CREATE TABLE IF NOT EXISTS public.organisation
(
    organisation_id text COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default",
    contact_name text COLLATE pg_catalog."default",
    contact_email text COLLATE pg_catalog."default",
    contact_phone text COLLATE pg_catalog."default",
    CONSTRAINT organisation_pkey PRIMARY KEY (organisation_id)
)

TABLESPACE pg_default;

ALTER TABLE public.organisation
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.licenses
(
    license_id text COLLATE pg_catalog."default" NOT NULL,
    organisation_id text COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    product_name text COLLATE pg_catalog."default" NOT NULL,
    license_type text COLLATE pg_catalog."default" NOT NULL,
    comment text COLLATE pg_catalog."default",
    CONSTRAINT licenses_pkey PRIMARY KEY (license_id),
    CONSTRAINT licenses_organisation_id_fkey FOREIGN KEY (organisation_id)
        REFERENCES public.organisation (organisation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.licenses
    OWNER to postgres;