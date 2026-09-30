CREATE TABLE crops (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    common_name             VARCHAR(80)  NOT NULL,
    scientific_name         VARCHAR(120),
    salinity_threshold_ds_m DECIMAL(5,2) NOT NULL,
    salt_tolerance_class    VARCHAR(30)  NOT NULL,
    source_reference        VARCHAR(200),
    CONSTRAINT uk_crops_common_name UNIQUE (common_name),
    CONSTRAINT ck_crops_threshold_positive CHECK (salinity_threshold_ds_m > 0)
);
