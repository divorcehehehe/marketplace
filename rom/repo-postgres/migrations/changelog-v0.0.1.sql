--liquibase formatted sql

--changeset sokatov:1 labels:v0.0.1
CREATE TYPE "model_visibility_type" AS ENUM ('public', 'owner');
CREATE TYPE "model_sampling_type" AS ENUM ('latin_hyper_cube', 'adaptive_sampling');

CREATE TABLE "models" (
	"id" text primary key,
	"lock" text not null,
	"owner_id" text not null,
	"name" text,
	"macro_path" text,
	"solver_path" text,
	"sampling" model_sampling_type,
	"params" jsonb,
	"us_vector" text[],
	"vt_vector" text[],
	"visibility" model_visibility_type
);
