create schema data;

use data;
create table property
(
    id        bigint auto_increment,
    property  bigint default 0   not null,
    item_id   bigint default 0   not null,
    timestamp bigint default 0   not null,
    average   double default 0.0 not null,
    value     text               not null,
    constraint property_pk
        primary key (id)
)
    comment '商品特征表';


create index item_time_property_index
    on property (item_id, timestamp, property);

create index property_index
    on property (property);

create table event
(
    id         bigint auto_increment,
    item_id    bigint default 0 not null,
    visitor_id bigint default 0 not null,
    timestamp  bigint default 0 not null,
    result     text             null,
    constraint event_pk
        primary key (id)
)
    comment '事件';

create index item_time_index
    on event (item_id, timestamp);

create index visitor_index
    on event (visitor_id);




