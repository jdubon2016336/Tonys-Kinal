# Javier Alejandro Dubón Ubedo

#ALTER USER 'root'@'localhost' identified WITH mysql_native_password BY 'admin';

drop database if exists DBTonysKinal2016336;

Create database DBTonysKinal2016336;
use DBTonysKinal2016336;

#-----------Creacion de Entidades------------
Create Table Empresas(
	codigoEmpresa int not null auto_increment,
	nombreEmpresa varchar (150) not null,
	direccion varchar (150) not null,
	telefono varchar (10) not null,
	primary key PK_codigoEmpresa (codigoEmpresa));
    
Create Table Presupuesto(
	codigoPresupuesto int not null auto_increment,
	fechaSolicitud Date not null,
	cantidadPresupuesto Decimal(10,2) not null default 0.00,
	codigoEmpresa int not null,
	primary key PK_codigoPresupuesto (codigoPresupuesto),
	constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
	descripcion varchar (100) not null,
	primary key PK_codigoTipoPlato (codigoTipoPlato));

Create Table Platos(
	codigoPlato int not null auto_increment,
	cantidad int not null default 0,
	nombrePlato varchar (100) not null,
	descripcionPlato varchar (150) not null,
	precioPlato decimal (10,2) not null default 0.00,
	codigoTipoPlato int,
	primary key PK_codigoPlato (codigoPlato),
	constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato) references TipoPlato(codigoTipoPlato) on delete cascade);

Create table Productos(
	codigoProducto int not null auto_increment,
	nombreProducto varchar (150) not null,
	cantidad int not null default 0,
	primary key PK_codigoProducto (codigoProducto));

Create Table Servicios(
	codigoServicio int not null auto_increment,
	fechaServicio date not null,
	tipoServicio varchar (100) not null,
	horaServicio time not null,	
	lugarServicio varchar (100) not null,
	telefonoContacto varchar (20) not null,
	codigoEmpresa int,
	primary key PK_codigoServicio (codigoServicio),
	constraint FK_Servicio_Empresas foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade);
	
Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
	descripcion varchar (100) not null,
	primary key PK_codigoTipoEmpleado (codigoTipoEmpleado));

Create table Empleados(
	codigoEmpleado int not null auto_increment, 
	numeroEmpleado int not null default 0,
	apellidosEmpleado varchar (150) not null,
	nombresEmpleado varchar (150) not null,
	direccionEmpleado varchar (150) not null,
	telefonoContacto varchar (10) not null,
	gradoCocinero varchar (50),
	codigoTipoEmpleado int not null,
	primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_Empleados_TipoEmpleado foreign key (codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade);

create table Servicios_has_Empleados(
	codigoServicio int,
    codigoEmpleado int,
	fechaEvento date not null,
    horaEvento time not null,
    lugarEvento Varchar(150) not null,
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
    constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado) references Empleados(codigoEmpleado) on delete cascade);

create table Productos_has_Platos(
	codigoProducto int,
    codigoPlato int,
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto) references Productos(codigoProducto) on delete cascade,
    constraint FK_Productos_has_Platos_Platos foreign key (codigoPlato) references Platos(CodigoPlato) on delete cascade);

create	table  Servicios_has_Platos(
	codigoServicio int,
    codigoPlato int,
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
    constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato) references Platos(codigoPlato) on delete cascade);
    
#--------- Procedimientos Almacenados ------------ 
#--------- Empresas ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarEmpresas()
		begin
			select em.codigoEmpresa, 
            em.nombreEmpresa,
            em.direccion, 
            em.telefono
            from Empresas em;
		end $$
delimiter ;

call sp_ListarEmpresas();

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarEmpresa(
	in nombreEmpresa varchar (150),
	in direccion varchar (150),
	in telefono varchar (10))	
    begin
		insert into Empresas(nombreEmpresa, direccion, telefono) values (nombreEmpresa, direccion, telefono);
	end$$
delimiter ;

call sp_AgregarEmpresa('Panaderia Trigo de oro', 'zona 11 de Mixco', '25140216');
call sp_AgregarEmpresa('Telas Novatex', 'zona 13', '71657863');
call sp_AgregarEmpresa('Zapateria Payless', 'zona 9', '43473587');
call sp_AgregarEmpresa('Grupo DIVECO', 'zona 10', '25140216');
call sp_AgregarEmpresa('Suplementos Alimenticios GNC', 'zona 21', '24054534');
call sp_AgregarEmpresa('INTEK Guatemala', 'Zona 5', '20125663');
call sp_AgregarEmpresa('Agencia de viaje Volare', 'Zona 9', '25063535');
call sp_AgregarEmpresa('Abba Servicios Técnicos', 'Zona 12', '53205404');
call sp_AgregarEmpresa('Acre Fabrica de muebles', 'Zona 9', '24327096');
call sp_AgregarEmpresa('Agencias Kabat', 'Zona 13', '23854808');

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarEmpresa(
    in ncodigoEmpresa int, 
    in nnombreEmpresa varchar (150), 
    in ndireccion varchar (150), 
    in ntelefono varchar (10))
	begin 
		update Empresas set nombreEmpresa = nnombreEmpresa,
							direccion = ndireccion,
							telefono = ntelefono
						where codigoEmpresa = ncodigoEmpresa;
	end $$
delimiter ;

call sp_ActualizarEmpresa()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarEmpresa(in variable int)
	begin
		delete from Empresas where Empresas.codigoEmpresa=variable;
	end $$
delimiter ;

call sp_EliminarEmpresa()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarEmpresa(in variable int)
		begin
			select codigoEmpresa, nombreEmpresa, direccion, telefono
            from Empresas where codigoEmpresa = variable;
		end $$
delimiter ;

call sp_BuscarEmpresa()
#---------------------------------------------------------------

#--------- Presupuesto ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarPresupuesto()
		begin
			select pr.codigoPresupuesto, 
            pr.fechaSolicitud, 
            pr.cantidadPresupuesto, 
            pr.codigoEmpresa
            from Presupuesto pr;
		end $$
delimiter ;

call sp_ListarPresupuesto()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarPresupuesto(
	in fechaSolicitud Date ,
	in cantidadPresupuesto Decimal(10,2), 
    in codigoEmpresa int) 
    begin
		insert into Presupuesto(fechaSolicitud, cantidadPresupuesto, codigoEmpresa) 
			values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
	end$$
delimiter ;

call sp_AgregarPresupuesto('2020-05-05',4500,1);
call sp_AgregarPresupuesto('2020-06-01',16000.0,3);
call sp_AgregarPresupuesto('2020-04-16',13500.0,2);
call sp_AgregarPresupuesto('2020-04-30',8500.65,4);
call sp_AgregarPresupuesto('2020-05-23',10350.0,5);
call sp_AgregarPresupuesto('2020-04-23',11300.0,9);
call sp_AgregarPresupuesto('2020-04-06',8900.5,7);
call sp_AgregarPresupuesto('2020-04-01',15300.75,6);
call sp_AgregarPresupuesto('2020-04-04',18400.0,8);
call sp_AgregarPresupuesto('2020-05-12',11000.0,10);

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarPresupuesto(in ncodigoPresupuesto int,
	in nfechaSolicitud Date ,
	in ncantidadPresupuesto Decimal(10,2))
	begin 
		update Presupuesto set fechaSolicitud = nfechaSolicitud, 
								cantidadPresupuesto = ncantidadPresupuesto
							where codigoPresupuesto = ncodigoPresupuesto;
	end $$
delimiter ;

call sp_ActualizarPresupuesto()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarPresupuesto(in variable int)
	begin
		delete from Presupuesto where Presupuesto.codigoPresupuesto=variable;
	end $$
delimiter ;

call sp_EliminarPresupuesto()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarPresupuesto(in variable int)
		begin
			select codigoPresupuesto, fechaSolicitud, cantidadPresupuesto, codigoEmpresa
            from Presupuesto where codigoPresupuesto = variable;
		end $$
delimiter ;

call sp_BuscarPresupuesto()
#---------------------------------------------------------------

#--------- Tipo Plato ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarTipoPlato()
		begin
			select tp.codigoTipoPlato, tp.descripcion
            from TipoPlato tp;
		end $$
delimiter ;

call sp_ListarTipoPlato()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarTipoPlato(in descripcion varchar (100))
    begin
		insert into TipoPlato(descripcion) values (descripcion);
	end$$
delimiter ;

call sp_AgregarTipoPlato('Ensalada');
call sp_AgregarTipoPlato('Sopa');
call sp_AgregarTipoPlato('Postre');
call sp_AgregarTipoPlato('Entrada');
call sp_AgregarTipoPlato('Plato fuerte');
call sp_AgregarTipoPlato('Bebida');

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarTipoPlato(in ncodigoTipoPlato int,
    in ndescripcion varchar (100))
	begin 
		update TipoPlato set descripcion = ndescripcion
						where codigoTipoPlato = ncodigoTipoPlato;
	end $$
delimiter ;

call sp_ActualizarTipoPlato()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarTipoPlato(in variable int)
	begin
		delete from TipoPlato where TipoPlato.codigoTipoPlato=variable;
	end $$
delimiter ;

call sp_EliminarTipoPlato()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarTipoPlato(in variable int)
		begin
			select codigoTipoPlato, descripcion
            from TipoPlato where codigoTipoPlato = variable;
		end $$
delimiter ;

call sp_BuscarTipoPlato()
#---------------------------------------------------------------

#--------- Platos ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarPlatos()
		begin
			select pl.codigoPlato,
            pl.cantidad,
            pl.nombrePlato, 
            pl.descripcionPlato, 
            pl.precioPlato,
            pl.codigoTipoPlato
            from Platos pl;
		end $$
delimiter ;

call sp_ListarPlatos()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarPlato(
	in cantidad int,
	in nombrePlato varchar (100),
	in descripcionPlato varchar (150),
	in precioPlato decimal (10,2),
	in codigoTipoPlato int)
    begin
		insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato) 
			values (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
	end$$
delimiter ;

call sp_AgregarPlato(125, 'Pasta Bologñesa', 'Pasta tipo Tortellini cubierta de salsa de tomate, romero y albahaca cubierda de queso parmesano', 55.00, 5);
call sp_AgregarPlato(125, 'Tiramisu', 'Biscocho de vainilla humedecido en una mezcla de café y licor de café con crema batida en capas espolvoreado con cacao en polvo', 30.00, 3);
call sp_AgregarPlato(100, 'Paella', 'Platillo de arroz de con vegetales demar y tierra; camaron, ostras, carne de codero, cerdo, res y pollo', 80.00, 5);
call sp_AgregarPlato(100, 'Ensalada Primavera', 'Mezcla de arugula, lechuga, calabacin, pimiento yaceitunas negras aderezado con aceite de de oliva y menta', 40.00, 1);
call sp_AgregarPlato(250, 'Sushi de Salmon', 'Salmon crudo acompañado de soya, aguacate y envuelto en arroz y cubierta de Nori', 50.00, 5);
call sp_AgregarPlato(250, 'Ramen de Cerdo', 'Sopa de fideos con soya, jengibre, hojas de mijo, puerro y cerdo asado', 45.00, 2);
call sp_AgregarPlato(200, 'Tacos "El Primo"', 'Toritilla de maiz con cerdo deshebrado en salsa roja  con piña, cebolla, cilantro y variedad de salsas', 35.00, 5);
call sp_AgregarPlato(100, 'Nachos "El Macho"', 'Nachos cubiertos de frijoles, tomate, cebolla, carne de cerdo deshebrada, guacamole, jalapeño, crema y salsa de queso cheddar', 80.00, 4);
call sp_AgregarPlato(125, 'Pescado y Papas fritas', 'Bacalao empanizado y frito acompañado de papas fritas recubiertas y aderezos para acompañar', 50.00, 5);
call sp_AgregarPlato(20, 'Free Bar', 'Una seleccion de cocteles y bebidas, tales como: "Modern English", "Born to be British", "Elderflower Cordial", entre otros ', 200.00, 6);


#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarPlato(in ncodigoPlato int,
	in ncantidad int,
	in nnombrePlato varchar (100),
	in ndescripcionPlato varchar (150),
	in nprecioPlato decimal (10,2))
	begin 
		update Platos set cantidad = ncantidad,
                            nombrePlato = nnombreplato,
                            descripcionPlato = ndescripcionplato,
                            precioPlato = nprecioPlato
						where codigoPlato = ncodigoPlato;
	end $$
delimiter ;

#call sp_ActualizarPlato(5, 250, 'Sushi de Salmon', 'Salmon crudo acompañado de soya, aguacate y envuelto en arroz y cubierta de Nori', 50.00);

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarPlato(in variable int)
	begin
		delete from Platos where Plato.codigoPlato=variable;
	end $$
delimiter ;

call sp_EliminarPlato()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarPlato(in variable int)
		begin
			select codigoPlato, cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato
            from Platos where codigoPlato = variable;
		end $$
delimiter ;

call sp_BuscarPlato()

#---------------------------------------------------------------

#--------- Productos ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarProductos()
		begin
			select pro.codigoProducto, pro.nombreProducto, pro.cantidad
            from Productos pro;
		end $$
delimiter ;

call sp_ListarProductos()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarProducto(
	in nombreProducto varchar (150),
	in cantidad int)
    begin
		insert into Productos(nombreProducto, cantidad) values (nombreProducto, cantidad);
	end$$
delimiter ;

call sp_AgregarProducto('Fideos', 1);
call sp_AgregarProducto('Tomate', 2);
call sp_AgregarProducto('Queso Parmesano', 1);
call sp_AgregarProducto('café', 3);
call sp_AgregarProducto('Crema batida', 1);
call sp_AgregarProducto('Cacao en polvo', 1);
call sp_AgregarProducto('Carne de Cerdo', 3);
call sp_AgregarProducto('Carne de Res', 3);
call sp_AgregarProducto('Pollo', 2);
call sp_AgregarProducto('Arugula', 1);
call sp_AgregarProducto('Aceitunas negras', 5);
call sp_AgregarProducto('Aceite de oliva', 1);
call sp_AgregarProducto('Salmon', 1);
call sp_AgregarProducto('Arroz', 1);
call sp_AgregarProducto('Nori', 1);
call sp_AgregarProducto('Soya', 2);
call sp_AgregarProducto('Puerro', 1);
call sp_AgregarProducto('Jengibre', 1);
call sp_AgregarProducto('Tortilla de maiz', 2);
call sp_AgregarProducto('Cebolla', 1);
call sp_AgregarProducto('Cilantro', 2);
call sp_AgregarProducto('Frijoles', 1);
call sp_AgregarProducto('Jalapeño', 3);
call sp_AgregarProducto('Aguacate', 2);
call sp_AgregarProducto('Bacalao', 1);
call sp_AgregarProducto('Papas', 4);
call sp_AgregarProducto('Hierbahuena', 1);
call sp_AgregarProducto('Menta', 1);
call sp_AgregarProducto('Limon', 1);
call sp_AgregarProducto('Hielo', 1);

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarProducto(in ncodigoProducto int,
    in nnombreProducto varchar (150), 
    in ncantidad int)
	begin 
		update Productos set nombreProducto = nnombreProducto,
                            cantidad = ncantidad
						where codigoProducto = ncodigoProducto;
	end $$
delimiter ;

call sp_ActualizarProducto()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarProducto(in variable int)
	begin
		delete from Productos where Productos.codigoProducto=variable;
	end $$
delimiter ;

call sp_EliminarProducto()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarProducto(in variable int)
		begin
			select codigoProducto, nombreProducto, cantidad
            from Productos where codigoProducto = variable;
		end $$
delimiter ;

call sp_BuscarProducto()

#---------------------------------------------------------------

#--------- Servicios ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarServicios()
		begin
			select se.codigoServicio,
            se.fechaServicio,
            se.tipoServicio,
            se.horaServicio,
            se.lugarServicio,
            se.telefonoContacto,
            se.codigoEmpresa
            from Servicios se;
		end $$
delimiter ;

call sp_ListarServicios()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarServicio(
	in fechaServicio date,
	in tipoServicio varchar (100),
	in horaServicio time,	
	in lugarServicio varchar (100),
	in telefonoContacto varchar (20),
	in codigoEmpresa int)
    begin
		insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa) 
				values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto,codigoEmpresa);
	end$$
delimiter ;

call sp_AgregarServicio('2020-05-12','Buffet','17:05:00','Zona 21','25140216', 1);
call sp_AgregarServicio('2020-06-10','Buffet','15:00:00','Zona 17','25140216', 4);
call sp_AgregarServicio('2020-05-11','Mesa Italiana','14:30:00','Zona 10','71657863', 2);
call sp_AgregarServicio('2020-05-29','Mesa Mexicana','19:45:00','Zona 4','43473587', 3);
call sp_AgregarServicio('2020-05-02','Mesa Española','07:30:00','Zona 11','24054534', 5);
call sp_AgregarServicio('2020-04-30','Mesa Española','20:00:00','Zona 10','53205404', 9);
call sp_AgregarServicio('2020-05-12','Mesa Japonesa','19:00:00','Zona 14','20125663', 7);
call sp_AgregarServicio('2020-04-08','Buffet','11:00:00','Zona 21','23854808', 6);
call sp_AgregarServicio('2020-04-11','Mesa Mexicana','12:30:00','Zona 18','25063535', 8);
call sp_AgregarServicio('2020-05-19','Mesa Italiana','13:00:00','Zona 10','24327096', 10);

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarServicio(in ncodigoServicio int,
	in nfechaServicio date,
	in ntipoServicio varchar (100),
	in nhoraServicio time,	
	in nlugarServicio varchar (100),
	in ntelefonoContacto varchar (20))
	begin 
		update Servicios set fechaServicio = nfechaServicio,
                            tipoServicio = ntipoServicio,
                            horaServicio = nhoraServicio,
                            lugarServicio = nlugarServicio, 
                            telefonoContacto = ntelefonoContacto
						where codigoServicio = ncodigoServicio;
	end $$
delimiter ;

call sp_ActualizarServicio()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarServicio(in variable int)
	begin
		delete from Servicios where Servicios.codigoServicio=variable;
	end $$
delimiter ;

call sp_EliminarServicio()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarServicio(in variable int)
		begin
			select codigoServicio, fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa
            from Servicios where codigoServicio = variable;
		end $$
delimiter ;

call sp_BuscarServicio()

#---------------------------------------------------------------

#--------- Tipo Empleado ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarTipoEmpleado()
		begin
			select te.codigoTipoEmpleado, te.descripcion
            from TipoEmpleado te;
		end $$
delimiter ;

call sp_ListarTipoEmpleado()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarTipoEmpleado(in descripcion varchar (100))
    begin
		insert into TipoEmpleado( descripcion) values (descripcion);
	end$$
delimiter ;

call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('BarTender');
call sp_AgregarTipoEmpleado('Guardia de seguridad');
call sp_AgregarTipoEmpleado('Recepcionista');
call sp_AgregarTipoEmpleado('Supervisor');
call sp_AgregarTipoEmpleado('Conserje');
call sp_AgregarTipoEmpleado('Asistente de Camarero');

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarTipoEmpleado(in ncodigoTipoEmpleado int, in ndescripcion varchar (100))
	begin 
		update TipoEmpleado set descripcion = ndescripcion
						where codigoTipoEmpleado = ncodigoTipoEmpleado;
	end $$
delimiter ;

call sp_ActualizarTipoEmpleado()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarTipoEmpleado(in variable int)
	begin
		delete from TipoEmpleado where TipoEmpleado.codigoTipoEmpleado=variable;
	end $$
delimiter ;

call sp_EliminarTipoEmpleado()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarTipoEmpleado(in variable int)
		begin
			select codigoTipoEmpleado, descripcion from TipoEmpleado where codigoTipoEmpleado = variable;
		end $$
delimiter ;

call sp_BuscarTipoEmpleado()

#---------------------------------------------------------------

#--------- Empleados ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarEmpleados()
		begin
			select epl.codigoEmpleado,
            epl.numeroEmpleado,
            epl.apellidosEmpleado,
            epl.nombresEmpleado,
            epl.direccionEmpleado,
            epl.telefonoContacto,
            epl.gradoCocinero,
            epl.codigoTipoEmpleado
            from Empleados epl;
		end $$
delimiter ;

call sp_ListarEmpleados()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarEmpleado( 
	in numeroEmpleado int,
	in apellidosEmpleado varchar (150),
	in nombresEmpleado varchar (150),
	in direccionEmpleado varchar (150),
	in telefonoContacto varchar (10),
	in gradoCocinero varchar (50),
	in codigoTipoEmpleado int)
    begin
		insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
			telefonoContacto, gradoCocinero, codigoTipoEmpleado) 
				values (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
					telefonoContacto, gradoCocinero, codigoTipoEmpleado);
	end$$
delimiter ;

call sp_AgregarEmpleado(12020,'Dubón Ubedo','Javier Alejandro','Zona 11 de Mixco','45206578','Chef',2);  
call sp_AgregarEmpleado(22020,'Hernandez Lopez','Allan Rodrigo','Zona 21','45120542','',5);  
call sp_AgregarEmpleado(32020,'Lopez Arreola','Cristian Gabriel','Zona 4','45952626','',1);  
call sp_AgregarEmpleado(122020,'Gonzalez Monroy','Denia Rebeca','Zona 11','56687410','Su-Chef',1);  
call sp_AgregarEmpleado(72020,'Morales Gomez','Francisco José','Zona 14','34505184','',6);  
call sp_AgregarEmpleado(102020,'Alvarez Moran','Ingrid Janeth ','Zona 12','54203261','',9);  
call sp_AgregarEmpleado(92020,'Guzman Veliz','Gerardo Rafael','Zona 11','54872906','',3);  
call sp_AgregarEmpleado(42020,'Estrada Perez','Ana Luisa','Zona 10','45780213','',7);  
call sp_AgregarEmpleado(112020,'Gimenez Giron','Gustavo Antonio','Zona 6','40101613','',4);  
call sp_AgregarEmpleado(82020,'Pineda Alvarado','Mildred Maribel ','Zona 12','45321059','',8);                                    

#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarEmpleado(in ncodigoEmpleado int, 
	in nnumeroEmpleado int,
	in napellidosEmpleado varchar (150),
	in nnombresEmpleado varchar (150),
	in ndireccionEmpleado varchar (150),
	in ntelefonoContacto varchar (10),
	in ngradoCocinero varchar (50))
	begin 
		update Empleados set numeroEmpleado = nnumeroEmpleado, 
							apellidosEmpleado = napellidosEmpleado,
							nombresEmpleado = nnombresEmpleado,
							direccionEmpleado = ndireccionEmpleado,
							telefonoContacto = ntelefonoContacto,
							gradoCocinero = ngradoCocinero
						where codigoEmpleado = ncodigoEmpleado;
	end $$
delimiter ;

call sp_ActualizarEmpleado()

#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarEmpleado(in variable int)
	begin
		delete from Empleados where Empleados.codigoEmpleado=variable;
	end $$
delimiter ;

call sp_EliminarEmpleado()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarEmpleado(in variable int)
		begin
			select codigoEmpleado, numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero,
				codigoTipoEmpleado
                    from Empleados where codigoEmpleado = variable;
		end $$
delimiter ;

call sp_BuscarEmpleado()

#---------------------------------------------------------------

#--------- Servicios has Empleados ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarServicios_has_Empleados()
		begin
			select she.codigoServicio, 
            she.codigoEmpleado, 
            she.fechaEvento, 
            she.horaEvento,
            she.lugarEvento
            from Servicios_has_Empleados she;
		end $$
delimiter ;

call sp_ListarServicios_has_Empleados();

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarServicios_has_Empleados(
	in codigoServicio int,
    in codigoEmpleado int,
	in fechaEvento date,
    in horaEvento time,
    in lugarEvento Varchar(150))
    begin
		insert into Servicios_has_Empleados(codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento) 
				values (codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
	end$$
delimiter ;

call sp_AgregarServicios_has_Empleados(1,3,'2020-05-12','17:05:00','Zona 21');
call sp_AgregarServicios_has_Empleados(2,2,'2020-06-10','15:00:00','Zona 17');
call sp_AgregarServicios_has_Empleados(3,1,'2020-05-11','14:30:00','Zona 10');
call sp_AgregarServicios_has_Empleados(4,10,'2020-05-29','19:45:00','Zona 4');
call sp_AgregarServicios_has_Empleados(5,9,'2020-05-02','07:30:00','Zona 11');
call sp_AgregarServicios_has_Empleados(6,8,'2020-04-30','20:00:00','Zona 10');
call sp_AgregarServicios_has_Empleados(7,7,'2020-05-12','19:00:00','Zona 14');
call sp_AgregarServicios_has_Empleados(8,6,'2020-04-08','11:00:00','Zona 21');
call sp_AgregarServicios_has_Empleados(9,5,'2020-04-11','12:30:00','Zona 18');
call sp_AgregarServicios_has_Empleados(10,4,'2020-05-19','13:00:00','Zona 10');


#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarServicios_has_Empleados(in codigoServicio int,
	in nfechaEvento date,
    in nhoraEvento time,
    in nlugarEvento Varchar(150))
	begin 
		update Servicios_has_Empleados set fechaEvento = nfechaEvento,
                                            horaEvento = nhoraEvento,
                                            lugarEvento = nlugarEvento
										where codigoServicio = codigoServicio;
	end $$
delimiter ;

call sp_ActualizarServicios_has_Empleados()

#drop procedure sp_EliminarServicios_has_Empleados;
#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarServicios_has_Empleados(in variable int)
	begin
		delete from Servicios_has_Empleados where Servicios_has_Empleados.codigoServicio=variable;
	end $$
delimiter ;

call sp_EliminarServicios_has_Empleados()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarServicios_has_Empleados(in variable int)
		begin
			select codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento
                    from Servicios_has_Empleados where codigoServicio = variable;
		end $$
delimiter ;

call sp_BuscarServicios_has_Empleados()

#---------------------------------------------------------------

#--------- Productos has Platos ----------  

#--------- Listar ----------
delimiter $$
	create procedure sp_ListarProductos_has_Platos()
		begin
			select php.codigoProducto,
            php.codigoPlato
            from Productos_has_Platos php;
		end $$
delimiter ;

call sp_ListarProductos_has_Platos()

#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarProductos_has_Platos(
	in codigoProducto int,
    in codigoPlato int)
    begin
		insert into Productos_has_Platos(codigoProducto, codigoPlato) values (codigoProducto, codigoPlato);
	end$$
delimiter ;

call sp_AgregarProductos_has_Platos(1, 1);
call sp_AgregarProductos_has_Platos(2, 1);
call sp_AgregarProductos_has_Platos(3, 1);
call sp_AgregarProductos_has_Platos(4, 2);
call sp_AgregarProductos_has_Platos(5, 2);
call sp_AgregarProductos_has_Platos(6, 2);
call sp_AgregarProductos_has_Platos(7, 3);
call sp_AgregarProductos_has_Platos(8, 3);
call sp_AgregarProductos_has_Platos(9, 3);
call sp_AgregarProductos_has_Platos(10, 4);
call sp_AgregarProductos_has_Platos(11, 4);
call sp_AgregarProductos_has_Platos(12, 4);
call sp_AgregarProductos_has_Platos(13, 5);
call sp_AgregarProductos_has_Platos(14, 5);
call sp_AgregarProductos_has_Platos(15, 5);
call sp_AgregarProductos_has_Platos(16, 6);
call sp_AgregarProductos_has_Platos(17, 6);
call sp_AgregarProductos_has_Platos(18, 6);
call sp_AgregarProductos_has_Platos(19, 7);
call sp_AgregarProductos_has_Platos(20, 7);
call sp_AgregarProductos_has_Platos(21, 7);
call sp_AgregarProductos_has_Platos(22, 8);
call sp_AgregarProductos_has_Platos(23, 8);
call sp_AgregarProductos_has_Platos(24, 8);
call sp_AgregarProductos_has_Platos(25, 9);
call sp_AgregarProductos_has_Platos(26, 9);
call sp_AgregarProductos_has_Platos(27, 9);
call sp_AgregarProductos_has_Platos(28, 10);
call sp_AgregarProductos_has_Platos(29, 10);
call sp_AgregarProductos_has_Platos(30, 10);

#drop procedure sp_ActualizarProductos_has_Platos;
#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarProductos_has_Platos(in ncodigoProducto int,
    in ncodigoPlato int)
	begin 
		update Productos_has_Platos set codigoPlato = ncodigoPlato
									where codigoProducto = ncodigoProducto;
	end $$
delimiter ;

call sp_ActualizarProductos_has_Platos()

#drop procedure sp_EliminarProductos_has_Platos;
#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarProductos_has_Platos(in variable int)
	begin
		delete from Productos_has_Platos where Productos_has_Platos.codigoProducto=variable;
	end $$
delimiter ;

call sp_EliminarProductos_has_Platos()

#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarProductos_has_Platos(in variable int)
		begin
			select codigoProducto, codigoPlato from Productos_has_Platos where codigoProducto = variable;
		end $$
delimiter ;

call sp_BuscarProductos_has_Platos()

#---------------------------------------------------------------
#drop procedure sp_ListarServicios_has_Platos;

#--------- Servicios has Platos ----------  
#--------- Listar ----------
delimiter $$
	create procedure sp_ListarServicios_has_Platos()
		begin
			select shp.codigoServicio,
            shp.codigoPlato
            from Servicios_has_Platos shp;
		end $$
delimiter ;

call sp_ListarServicios_has_Platos();

#drop procedure sp_AgregarServicios_has_Platos;
#--------- Agregar ----------- 
delimiter $$    
create procedure sp_AgregarServicios_has_Platos(
	in codigoServicio int,
    in codigoPlato int)
    begin
		insert into Servicios_has_Platos(codigoServicio, codigoPlato) values (codigoServicio, codigoPlato);
	end$$
delimiter ;

call sp_AgregarServicios_has_Platos(1,9);
call sp_AgregarServicios_has_Platos(1,10);
call sp_AgregarServicios_has_Platos(2,9);
call sp_AgregarServicios_has_Platos(2,10);
call sp_AgregarServicios_has_Platos(3,1);
call sp_AgregarServicios_has_Platos(3,2);
call sp_AgregarServicios_has_Platos(4,7);
call sp_AgregarServicios_has_Platos(4,8);
call sp_AgregarServicios_has_Platos(5,3);
call sp_AgregarServicios_has_Platos(5,4);
call sp_AgregarServicios_has_Platos(6,3);
call sp_AgregarServicios_has_Platos(6,4);
call sp_AgregarServicios_has_Platos(7,5);
call sp_AgregarServicios_has_Platos(7,6);
call sp_AgregarServicios_has_Platos(8,9);
call sp_AgregarServicios_has_Platos(8,10);
call sp_AgregarServicios_has_Platos(9,7);
call sp_AgregarServicios_has_Platos(9,8);
call sp_AgregarServicios_has_Platos(10,1);
call sp_AgregarServicios_has_Platos(10,2);

#drop procedure sp_ActualizarServicios_has_Platos;
#-------- Actualizar --------
delimiter $$
	create procedure sp_ActualizarServicios_has_Platos(in ncodigoServicio int,
    in ncodigoPlato int)
	begin 
		update Servicios_has_Platos set codigoPlato = ncodigoPlato
									where codigoServicio = ncodigoServicio;
	end $$
delimiter ;

call sp_ActualizarServicios_has_Platos()

#drop procedure sp_EliminarServicios_has_Platos;
#------ Eliminar ----------
delimiter $$
	create procedure sp_EliminarServicios_has_Platos(in variable int)
	begin
		delete from Servicios_has_Platos where Servicios_has_Platos.codigoServicio=variable;
	end $$
delimiter ;

call sp_EliminarServicios_has_Platos()

#drop procedure sp_BuscarServicios_has_Platos;
#-------- Buscar ---------
delimiter $$
	create procedure sp_BuscarServicios_has_Platos(in variable int)
		begin
			select codigoServicio, codigoPlato from Servicios_has_Platos where codigoServicio = variable;
		end $$
delimiter ;

call sp_BuscarServicios_has_Platos()


delimiter $$
create procedure sp_ListarSHP()
	begin
		select s.codigoServicio, p.codigoPlato From Servicios s, Platos p;
	end$$
delimiter ;

call sp_ListarSHP();

delimiter $$
create procedure sp_ListarPHP()
	begin
		select pr.codigoProducto, pl.codigoPlato from Productos pr, Platos pl; 
	end$$
delimiter ; 

call sp_ListarPHP();   

#------------- Procedimientos para los Reportes ---------------
#--------- Reporte Presupuesto -------------

delimiter $$
create procedure sp_SubReportePresupuesto(in variable int)
	begin
		select * from Empresas e inner join Presupuesto p on e.codigoEmpresa = p.codigoEmpresa
			where e.codigoEmpresa = variable group by p.cantidadPresupuesto;
    end$$    
delimiter ;

call sp_SubReportePresupuesto()

delimiter $$
create procedure sp_ReportePresupuesto(in variable int)
	begin
		select * from Empresas e inner join Servicios s on e.codigoEmpresa = s.codigoServicio 
			where e.codigoEmpresa = variable;
    end$$
delimiter ;

call sp_ReportePresupuesto()

#-------------- Reporte Servicio -------------
Delimiter $$
    Create procedure sp_SubReporteServicio (in variable int)
        Begin
            Select * from Servicios s inner join servicios_has_platos shp on s.codigoServicio = shp.codigoServicio inner join productos_has_platos php
            on shp.codigoPlato = php.codigoPlato inner join productos p on p.codigoProducto = php.codigoProducto where s.codigoServicio = variable;
        End$$
Delimiter ;
call sp_SubReporteServicio()
 
 
Delimiter $$
    Create procedure sp_ReporteServicio (in variable int)
        Begin 
            Select * from servicios s inner join servicios_has_platos shp on s.codigoServicio = shp.codigoServicio inner join platos pl 
            on pl.codigoPlato = shp.codigoPlato inner join tipoPlato tp on tp.codigoTipoPlato = pl.codigoTipoPlato
            inner join productos_has_platos php on shp.codigoPlato = php.codigoPlato inner join productos p on p.codigoProducto = php.codigoProducto
            where s.codigoServicio = variable;
        End$$
Delimiter ;

call sp_ReporteServicio()



/*
Delimiter $$
    Create procedure sp_SubReporteServicio (in variable int)
        Begin
            Select p.nombreProducto from Servicios s inner join Servicios_has_Platos shp on s.codigoServicio = shp.codigoServicio inner join Productos_has_Platos php
            on shp.codigoPlato = php.codigoPlato inner join Productos p on p.codigoProducto = php.codigoProducto where s.codigoServicio = variable;
        End$$
Delimiter ;

drop procedure sp_SubReporteServicio;
call sp_SubReporteServicio(1);

Delimiter $$
    Create procedure sp_ReporteServicio (in variable int)
        Begin 
            Select s.tipoServicio, s.lugarServicio, s.fechaServicio, pl.cantidad, tp.descripcion, p.nombreProducto from servicios s inner join servicios_has_platos shp 
				on s.codigoServicio = shp.codigoServicio inner join platos pl on pl.codigoPlato = shp.codigoPlato inner join tipoPlato tp on tp.codigoTipoPlato = pl.codigoTipoPlato
				inner join productos_has_platos php on shp.codigoPlato = php.codigoPlato inner join productos p on p.codigoProducto = php.codigoProducto
					where s.codigoServicio = variable;
        End$$
Delimiter ;

drop procedure sp_ReporteServicio;
call sp_ReporteServicio(1);

select * from Platos pl inner join TipoPlato tp on pl.codigoTipoPlato = tp.codigoTipoPlato;

select * from Platos pl inner join Productos_has_platos php on pl.codigoPlato = php.codigoPlato inner join Productos pr on pr.codigoProducto = php.codigoProducto inner join TipoPlato tp on pl.codigoTipoPlato = tp.codigoTipoPlato  
	 inner join Servicios_has_Platos shp on pl.codigoPlato = shp.codigoPlato inner join Servicios s on s.codigoServicio = shp.codigoServicio;
    
    
#select * from Servicios s inner join Servicios_has_Platos shp on s.codigoServicio = shp.codigoServicio;
select * from Productos pr inner join Productos_has_Platos php on pr.codigoProducto = php.codigoProducto;
select * from Platos pl inner join Productos_has_platos php on pl.codigoPlato = php.codigoPlato;
/*Delimiter $$
create procedure sp_ListarReporte(in variable int)
	Begin
		select * from Empresas e inner join Presupuesto p on e.codigoEmpresa = p.codigoEmpresa
			inner join Servicios s on s.codigoEmpresa = e.codigoEmpresa 
				where e.codigoEmpresa = variable order by p.cantidadPresupuesto;
	End$$        
Delimiter ;    
call sp_ListarReporte(1);*/
/*select p.cantidad, tp.descripcion, p.nombrePlato, pr.nombreProducto from Platos p, TipoPlato tp, Productos pr, Servicios s, Servicios_has_Platos shp, Productos_has_Platos php
			where p.codigoTipoPlato = tp.codigoTipoPlato and pr.codigoProducto = php.codigoProducto and p.codigoPlato = php.codigoPlato and
				s.codigoServicio = shp.codigoServicio and p.codigoPlato = shp.codigoPlato and s.codigoServicio = variable;
         select S.tipoServicio,Pl.cantidad, Tp.descripcion, Pr.nombreProducto from TipoPlato Tp
        inner join Platos Pl on
			Tp.codigoTipoPlato = Pl.codigoTipoPlato
		inner join Servicios S on
			Pl.codigoTipoPlato = S.codigoServicio
		inner join Productos Pr
				where S.codigoServicio = variable;   */ 
               /*  select distinct s.codigoServicio, s.tipoServicio,pl.cantidad, pl.nombrePlato, tp.descripcion, pr.nombreProducto from TipoPlato tp
        inner join Platos pl on
			tp.codigoTipoPlato = pl.codigoTipoPlato
		inner join Servicios s on
			pl.codigoTipoPlato = s.codigoServicio
		inner join Productos as Pr
				where S.codigoServicio = 1 and pr.codigoProducto = 1+5;
                
delimiter $$
create procedure sp_ReporteServicio(in variable int)
	begin
		select * from TipoPlato tp inner join Platos P on
		tp.codigoTipoPlato = P.codigoTipoPlato inner join Servicios S on
		P.codigoTipoPlato = S.codigoServicio inner join productos Pr on
		P.codigoTipoPlato = PR.codigoProducto
		where S.codigoServicio = variable;
	end$$
delimiter ;

call sp_ReporteServicio(1);*/
#---------------------------------------------------------------