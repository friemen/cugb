-- name: manufacturer-by-id
-- Reads at most 1 manufacturer matching id.
SELECT *
FROM manufacturer
WHERE id = :id


-- name: manufacturers
-- Read all manufacturers.
SELECT *
FROM manufacturer
