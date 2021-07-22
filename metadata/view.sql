
CREATE EXTENSION postgis SCHEMA public VERSION '2.4.7';

----------------------- prod script ------------------------
-- CREATE SCHEMA supermun AUTHORIZATION peogo;
-- CREATE EXTENSION postgis VERSION '2.4.7';
-- ALTER TABLE supermun.commune ALTER COLUMN geom TYPE geometry;
-- CREATE OR REPLACE VIEW commune_geojson AS SELECT row_to_json(fc) AS geom FROM
--    (SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM
--        (SELECT 'Feature' As type, ST_AsGeoJSON(ST_Transform(com.geom, 4326),15,0)::json As geometry,row_to_json(
--            (select r from
--                (select com.id, com.libelle, com.population, com.superficie, com.position_label_lat, com.position_label_lon, com.province_id, prov.region_id) r)
--            ) As properties
--         FROM supermun.commune As com, supermun.province As prov where com.province_id = prov.id and com.deleted = false) As f ) As fc;
------------------------------------------------------------

ALTER TABLE commune ALTER COLUMN geom TYPE supermun.geometry;

CREATE OR REPLACE VIEW supermun.commune_geojson AS SELECT row_to_json(fc) AS geom FROM
(SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features FROM
 (SELECT 'Feature' As type, supermun.ST_AsGeoJSON(supermun.ST_Transform(com.geom, 4326),15,0)::json As geometry,row_to_json(
	 (select r from
	  (select com.id, com.libelle, com.population, com.superficie, com.position_label_lat, com.position_label_lon, com.province_id, prov.region_id) r)
 ) As properties
FROM supermun.commune As com, supermun.province As prov where com.province_id = prov.id and com.deleted = false) As f ) As fc;
