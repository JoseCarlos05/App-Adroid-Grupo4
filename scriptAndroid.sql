create database comiProyecto;
use comiProyecto;
create type estado_enum as enum ('Tonificar', 'Bajar de peso', 'Ganar masa muscular');

alter type estado_enum owner to postgres;

create table usuarios
(
    id        serial
        constraint usuarios_pk
            primary key,
    nombre    varchar(45),
    correo    varchar(100) not null,
    telefono  char(9),
    altura    double precision,
    peso      double precision,
    fecha_nac timestamp,
    objetivo  estado_enum
);

alter table usuarios
    owner to postgres;

create table comida
(
    id            serial
        constraint comida_pk
            primary key,
    nombre        varchar(45),
    calorias      double precision,
    proteinas     double precision,
    carbohidratos double precision,
    vitaminas     double precision,
    grasas        double precision,
    minerales     double precision
);

alter table comida
    owner to postgres;

create table deporte
(
    id       serial
        constraint deporte_pk
            primary key,
    nombre   varchar(50),
    objetivo estado_enum
);

alter table deporte
    owner to postgres;

create table usuario_comida
(
    id           serial
        constraint usuario_comida_pk
            primary key,
    "id-usuario" integer
        constraint usuario_comida_usuarios_id_fk
            references usuarios,
    "id-comida"  integer
        constraint usuario_comida___fk
            references comida,
    fecha        timestamp,
    cantidad     integer
);

alter table usuario_comida
    owner to postgres;

create table usuario_deporte
(
    "id-usuario" integer
        constraint usuario_deporte_usuarios_id_fk
            references usuarios,
    "id-deporte" integer
        constraint usuario_deporte_deporte_id_fk
            references deporte
);

alter table usuario_deporte
    owner to postgres;


