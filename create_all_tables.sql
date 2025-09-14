-- PostgreSQL Database Schema for Coupon Management System
-- Complete script to create all tables manually in the coupons schema
-- Run this script in pgAdmin 4 SQL Query Tool

-- ================================================================
-- CREATE SCHEMA AND DROP EXISTING TABLES/SEQUENCES (if they exist)
-- ================================================================
CREATE SCHEMA IF NOT EXISTS coupons;

DROP TABLE IF EXISTS coupons.bxgy_get_products CASCADE;
DROP TABLE IF EXISTS coupons.bxgy_buy_products CASCADE;
DROP TABLE IF EXISTS coupons.bxgy_coupon CASCADE;
DROP TABLE IF EXISTS coupons.product_wise_coupon CASCADE;
DROP TABLE IF EXISTS coupons.cart_wise_coupon CASCADE;
DROP SEQUENCE IF EXISTS coupons.hibernate_sequence CASCADE;

-- ================================================================
-- CREATE SEQUENCE for ID generation
-- ================================================================
CREATE SEQUENCE coupons.hibernate_sequence START 1 INCREMENT 1;

-- ================================================================
-- 1. CART_WISE_COUPON TABLE
-- ================================================================
CREATE TABLE coupons.cart_wise_coupon (
    id BIGINT PRIMARY KEY DEFAULT nextval('coupons.hibernate_sequence'),
    code VARCHAR(255) UNIQUE NOT NULL,
    expiration_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    coupon_type VARCHAR(50) NOT NULL DEFAULT 'CART_WISE',
    threshold DECIMAL(10,2) NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL,

    -- Constraints
    CONSTRAINT chk_cart_threshold_positive CHECK (threshold > 0),
    CONSTRAINT chk_cart_discount_range CHECK (discount_percentage > 0 AND discount_percentage <= 100)
);

-- ================================================================
-- 2. PRODUCT_WISE_COUPON TABLE
-- ================================================================
CREATE TABLE coupons.product_wise_coupon (
    id BIGINT PRIMARY KEY DEFAULT nextval('coupons.hibernate_sequence'),
    code VARCHAR(255) UNIQUE NOT NULL,
    expiration_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    coupon_type VARCHAR(50) NOT NULL DEFAULT 'PRODUCT_WISE',
    product_id BIGINT NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL,

    -- Constraints
    CONSTRAINT chk_product_discount_range CHECK (discount_percentage > 0 AND discount_percentage <= 100)
);

-- ================================================================
-- 3. BXGY_COUPON TABLE
-- ================================================================
CREATE TABLE coupons.bxgy_coupon (
    id BIGINT PRIMARY KEY DEFAULT nextval('coupons.hibernate_sequence'),
    code VARCHAR(255) UNIQUE NOT NULL,
    expiration_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT,
    coupon_type VARCHAR(50) NOT NULL DEFAULT 'BXGY',
    repetition_limit INTEGER NOT NULL,

    -- Constraints
    CONSTRAINT chk_bxgy_repetition_positive CHECK (repetition_limit > 0)
);

-- ================================================================
-- 4. BXGY_BUY_PRODUCTS TABLE (ElementCollection for buyProducts Map)
-- ================================================================
CREATE TABLE coupons.bxgy_buy_products (
    coupon_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,

    PRIMARY KEY (coupon_id, product_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons.bxgy_coupon(id) ON DELETE CASCADE,

    -- Constraints
    CONSTRAINT chk_buy_quantity_positive CHECK (quantity > 0)
);

-- ================================================================
-- 5. BXGY_GET_PRODUCTS TABLE (ElementCollection for getProducts Map)
-- ================================================================
CREATE TABLE coupons.bxgy_get_products (
    coupon_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,

    PRIMARY KEY (coupon_id, product_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons.bxgy_coupon(id) ON DELETE CASCADE,

    -- Constraints
    CONSTRAINT chk_get_quantity_positive CHECK (quantity > 0)
);

-- ================================================================
-- CREATE INDEXES for Performance Optimization
-- ================================================================

-- Indexes for coupon code lookups (used frequently)
CREATE INDEX idx_cart_wise_code ON coupons.cart_wise_coupon(code);
CREATE INDEX idx_product_wise_code ON coupons.product_wise_coupon(code);
CREATE INDEX idx_bxgy_code ON coupons.bxgy_coupon(code);

-- Indexes for active coupon queries
CREATE INDEX idx_cart_wise_active_expiry ON coupons.cart_wise_coupon(is_active, expiration_date);
CREATE INDEX idx_product_wise_active_expiry ON coupons.product_wise_coupon(is_active, expiration_date);
CREATE INDEX idx_bxgy_active_expiry ON coupons.bxgy_coupon(is_active, expiration_date);

-- Index for product-wise coupon queries by product_id
CREATE INDEX idx_product_wise_product_id ON coupons.product_wise_coupon(product_id);

-- Indexes for BXGY product mappings
CREATE INDEX idx_bxgy_buy_product_id ON coupons.bxgy_buy_products(product_id);
CREATE INDEX idx_bxgy_get_product_id ON coupons.bxgy_get_products(product_id);

-- ================================================================
-- INSERT SAMPLE DATA (Optional - for testing)
-- ================================================================

-- Sample Cart-Wise Coupons
INSERT INTO coupons.cart_wise_coupon (code, expiration_date, is_active, description, coupon_type, threshold, discount_percentage) VALUES
('CART10', '2025-12-31', TRUE, '10% off on cart value above $100', 'CART_WISE', 100.00, 10.00),
('CART20', '2025-12-31', TRUE, '20% off on cart value above $200', 'CART_WISE', 200.00, 20.00);

-- Sample Product-Wise Coupons
INSERT INTO coupons.product_wise_coupon (code, expiration_date, is_active, description, coupon_type, product_id, discount_percentage) VALUES
('PROD15', '2025-12-31', TRUE, '15% off on Product ID 1001', 'PRODUCT_WISE', 1001, 15.00),
('PROD25', '2025-12-31', TRUE, '25% off on Product ID 1002', 'PRODUCT_WISE', 1002, 25.00);

-- Sample BxGy Coupon
INSERT INTO coupons.bxgy_coupon (code, expiration_date, is_active, description, coupon_type, repetition_limit) VALUES
('BOGO', '2025-12-31', TRUE, 'Buy 2 Get 1 Free', 'BXGY', 3);

-- Get the coupon_id for the BOGO coupon and insert related products
DO $$
DECLARE
    bogo_coupon_id BIGINT;
BEGIN
    SELECT id INTO bogo_coupon_id FROM coupons.bxgy_coupon WHERE code = 'BOGO';

    -- Insert buy products (Buy 2 of product 2001)
    INSERT INTO coupons.bxgy_buy_products (coupon_id, product_id, quantity) VALUES
    (bogo_coupon_id, 2001, 2);

    -- Insert get products (Get 1 free of product 2001)
    INSERT INTO coupons.bxgy_get_products (coupon_id, product_id, quantity) VALUES
    (bogo_coupon_id, 2001, 1);
END $$;

-- ================================================================
-- VERIFICATION QUERIES
-- ================================================================

-- Verify tables were created successfully
SELECT 'cart_wise_coupon' as table_name, COUNT(*) as record_count FROM coupons.cart_wise_coupon
UNION ALL
SELECT 'product_wise_coupon' as table_name, COUNT(*) as record_count FROM coupons.product_wise_coupon
UNION ALL
SELECT 'bxgy_coupon' as table_name, COUNT(*) as record_count FROM coupons.bxgy_coupon
UNION ALL
SELECT 'bxgy_buy_products' as table_name, COUNT(*) as record_count FROM coupons.bxgy_buy_products
UNION ALL
SELECT 'bxgy_get_products' as table_name, COUNT(*) as record_count FROM coupons.bxgy_get_products;

-- Show sequence current value
SELECT 'hibernate_sequence' as sequence_name, last_value FROM coupons.hibernate_sequence;

-- Show all table structures
SELECT
    table_schema,
    table_name,
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_schema = 'coupons'
AND table_name IN ('cart_wise_coupon', 'product_wise_coupon', 'bxgy_coupon', 'bxgy_buy_products', 'bxgy_get_products')
ORDER BY table_name, ordinal_position;

-- Show the schema was created
SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'coupons';