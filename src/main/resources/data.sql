insert into gateway (serial_number, name, ipv4)
values ('123', 'Gateway 1', '10.1.1.1');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid1', 'Vendor 1', now(), 0, '123');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid2', 'Vendor 2', now(), 0, '123');
insert into gateway (serial_number, name, ipv4)
values ('124', 'Gateway 2', '192.1.10.124');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid3', 'Vendor 3', now(), 0, '124');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid4', 'Vendor 4', now(), 0, '124');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid5', 'Vendor 5', now(), 0, '124');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid6', 'Vendor 6', now(), 0, '124');
insert into peripheral_device (UID, vendor, created, status, gateway)
values ('uid7', 'Vendor 7', now(), 0, '124');
insert into gateway (serial_number, name, ipv4)
values ('125', 'Gateway 3', '8.8.8.4');