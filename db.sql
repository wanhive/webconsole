--
-- PostgreSQL database dump
--

-- Dumped from database version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: wh_domain_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.wh_domain_seq
    START WITH 65536
    INCREMENT BY 1024
    MINVALUE 65536
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: wh_domain; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wh_domain (
    uid bigint DEFAULT nextval('public.wh_domain_seq'::regclass) NOT NULL,
    useruid bigint NOT NULL,
    createdon timestamp with time zone DEFAULT now() NOT NULL,
    modifiedon timestamp with time zone DEFAULT now() NOT NULL,
    name character varying(128),
    description character varying(256),
    type integer DEFAULT 0 NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    flag integer DEFAULT 0 NOT NULL
);


--
-- Name: wh_thing_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.wh_thing_seq
    START WITH 65536
    INCREMENT BY 1
    MINVALUE 65536
    NO MAXVALUE
    CACHE 1;


--
-- Name: wh_thing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wh_thing (
    uid bigint DEFAULT nextval('public.wh_thing_seq'::regclass) NOT NULL,
    domainuid bigint NOT NULL,
    createdon timestamp with time zone DEFAULT now() NOT NULL,
    modifiedon timestamp with time zone DEFAULT now() NOT NULL,
    name character varying(128) NOT NULL,
    salt character varying(128),
    verifier character varying(2048),
    type integer DEFAULT 0 NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    flag integer DEFAULT 0 NOT NULL
);


--
-- Name: wh_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wh_user (
    uid bigint NOT NULL,
    createdon timestamp with time zone DEFAULT now() NOT NULL,
    modifiedon timestamp with time zone DEFAULT now() NOT NULL,
    alias character varying(128) NOT NULL,
    email character varying(256) NOT NULL,
    password character varying(512),
    type integer DEFAULT 0 NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    flag integer DEFAULT 0 NOT NULL,
    token character varying(256),
    tokentimestamp timestamp with time zone,
    maxdomains bigint DEFAULT 0 NOT NULL,
    maxthings bigint DEFAULT 0 NOT NULL
);


--
-- Name: wh_user_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wh_user_token (
    uid bigint NOT NULL,
    token character varying(256) NOT NULL,
    modifiedon timestamp with time zone DEFAULT now() NOT NULL,
    usedon timestamp with time zone DEFAULT now() NOT NULL
);


--
-- Name: wh_user_uid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.wh_user_uid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: wh_user_uid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.wh_user_uid_seq OWNED BY public.wh_user.uid;


--
-- Name: wh_user uid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user ALTER COLUMN uid SET DEFAULT nextval('public.wh_user_uid_seq'::regclass);


--
-- Name: wh_domain wh_domain_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_domain
    ADD CONSTRAINT wh_domain_pkey PRIMARY KEY (uid);


--
-- Name: wh_thing wh_thing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_thing
    ADD CONSTRAINT wh_thing_pkey PRIMARY KEY (uid);


--
-- Name: wh_user wh_user_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user
    ADD CONSTRAINT wh_user_email_key UNIQUE (email);


--
-- Name: wh_user wh_user_email_key1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user
    ADD CONSTRAINT wh_user_email_key1 UNIQUE (email);


--
-- Name: wh_user wh_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user
    ADD CONSTRAINT wh_user_pkey PRIMARY KEY (uid);


--
-- Name: wh_user_token wh_user_token_token_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user_token
    ADD CONSTRAINT wh_user_token_token_key UNIQUE (token);


--
-- Name: wh_user_token wh_user_token_uid_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user_token
    ADD CONSTRAINT wh_user_token_uid_key UNIQUE (uid);


--
-- Name: fki_wh_thing_domain_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_wh_thing_domain_fk ON public.wh_thing USING btree (domainuid);


--
-- Name: wh_thing wh_thing_domain_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_thing
    ADD CONSTRAINT wh_thing_domain_fk FOREIGN KEY (domainuid) REFERENCES public.wh_domain(uid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: wh_user_token wh_user_token_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wh_user_token
    ADD CONSTRAINT wh_user_token_user_fk FOREIGN KEY (uid) REFERENCES public.wh_user(uid) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

