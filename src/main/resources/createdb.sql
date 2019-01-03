create table grad  {
    id int primary key,
    naziv text,
    broj_stanovnika int,
    drzava int
};

insert into grad (id, naziv, broj_stanovnika, drzava) values (1, "Pariz", 2200000, 1);
insert into grad (id, naziv, broj_stanovnika, drzava) values (2, "London", 8136000, 2);
insert into grad (id, naziv, broj_stanovnika, drzava) values (3, "Bec", 1868000, 3);
insert into grad (id, naziv, broj_stanovnika, drzava) values (4, "Manchester", 510746, 4);
insert into grad (id, naziv, broj_stanovnika, drzava) values (5, "Graz", 283869, 5);

create table drzava {
    id int primary key,
    naziv text,
    glavni_grad int references grad(id)
};

insert into drzava (id, naziv, glavni_grad) values (1, "Francuska", 1);
insert into drzava (id, naziv, glavni_grad) values (2, "Velika Britanija", 2);
insert into drzava (id, naziv, glavni_grad) values (3, "Austrija", 3);
