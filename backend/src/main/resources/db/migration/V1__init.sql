-- Core schema for SGEU CAD backend

create table if not exists stations (
  id uuid primary key,
  name varchar(120) not null,
  address_line varchar(240),
  city varchar(120),
  zone varchar(64),
  latitude double precision,
  longitude double precision,
  constraint ux_station_name unique (name)
);

create table if not exists units (
  id uuid primary key,
  agency varchar(16) not null,
  callsign varchar(32) not null,
  home_station_id uuid not null references stations(id),
  status varchar(24) not null,
  active_personnel integer not null,
  fuel_percent integer not null,
  last_address_line varchar(240),
  last_city varchar(120),
  last_zone varchar(64),
  last_latitude double precision,
  last_longitude double precision,
  available_from timestamptz,
  constraint ux_unit_callsign unique (callsign),
  constraint ck_unit_fuel_percent check (fuel_percent between 0 and 100),
  constraint ck_unit_active_personnel check (active_personnel >= 0)
);

create table if not exists emergencies (
  id uuid primary key,
  type varchar(16) not null,
  severity varchar(8) not null,
  address_line varchar(240),
  city varchar(120),
  zone varchar(64),
  latitude double precision,
  longitude double precision,
  reported_at timestamptz not null,
  estimated_initial_response_minutes integer not null,
  notes varchar(2000),
  constraint ck_emergency_estimated_response check (estimated_initial_response_minutes >= 0)
);

create table if not exists incidents (
  id uuid primary key,
  emergency_id uuid not null references emergencies(id),
  status varchar(24) not null,
  created_at timestamptz not null,
  resolved_at timestamptz,
  priority_score integer not null,
  constraint ux_incident_emergency unique (emergency_id),
  constraint ck_incident_priority check (priority_score >= 0)
);

create table if not exists dispatches (
  id uuid primary key,
  incident_id uuid not null references incidents(id),
  confirmed_by varchar(120) not null,
  confirmed_at timestamptz not null,
  eta_minutes integer not null,
  constraint ck_dispatch_eta check (eta_minutes >= 0)
);

create table if not exists assignments (
  id uuid primary key,
  incident_id uuid not null references incidents(id),
  unit_id uuid not null references units(id),
  dispatch_id uuid references dispatches(id),
  role varchar(16) not null,
  status varchar(16) not null,
  assigned_at timestamptz not null,
  released_at timestamptz,
  active boolean not null,
  constraint ck_assignment_active_release check (
    (active = true and released_at is null) or (active = false)
  )
);

-- Prevent multiple "active" assignments for the same unit
create unique index if not exists ux_unit_one_active_assignment
  on assignments(unit_id)
  where active = true;

create table if not exists incident_events (
  id uuid primary key,
  incident_id uuid not null references incidents(id),
  type varchar(32) not null,
  at timestamptz not null,
  actor varchar(120) not null,
  payload jsonb not null
);

create index if not exists ix_incident_events_incident_at
  on incident_events(incident_id, at);

