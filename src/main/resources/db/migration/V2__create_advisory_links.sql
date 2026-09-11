CREATE TABLE advisory_links (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    advisor_id   BIGINT      NOT NULL REFERENCES user_accounts (id),
    farmer_id    BIGINT      NOT NULL REFERENCES user_accounts (id),
    status       VARCHAR(20) NOT NULL,
    requested_at TIMESTAMP   NOT NULL,
    responded_at TIMESTAMP,
    CONSTRAINT ck_advisory_links_status CHECK (status IN ('PENDING', 'ACCEPTED', 'REVOKED'))
);

-- Prevents duplicate active supervision links between the same advisor and farmer.
-- A REVOKED link does not count as active, so the pair may be requested again.
CREATE UNIQUE INDEX uk_advisory_links_active_pair
    ON advisory_links (advisor_id, farmer_id)
    WHERE status <> 'REVOKED';
