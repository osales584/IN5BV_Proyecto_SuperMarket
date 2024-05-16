-- drop database if exists DBKinalSuperMaker;
create database DBKinalSuperMaker;

use DBKinalSuperMaker;

-- Primeras 5 entidades independientes
create table Clientes(
	codigoCliente int not null,
	nombreCliente varchar (50) not null,
	apellidoCliente varchar (50) not null,
    NITCliente varchar(10) not null,
	telefonoCliente varchar(8) not null,
    direccionCliente varchar (150) not null,
	correoCliente varchar (45),
	primary Key PK_codigoCliente (codigoCliente)
);

create table Proveedores(
	codigoProveedor int not null, 
	NITProveedor varchar (10) not null,
	nombreProveedor varchar(60) not null,
	apellidoProveedor varchar(60) not null,
	direccionProveedor varchar (150) not null,
	razonSocial varchar (60) not null,
	contactoPrincipal varchar (100) not null,
	paginaWeb varchar (50) not null, 
	primary Key PK_codigoProveedor (codigoProveedor)
);

create table Compras (
	numeroDocumento int not null,
	fechaDocumento date,
    descripcion varchar (60),
	totalDocumento decimal (10,2),
    primary key PK_numeroDocumento (numeroDocumento)
);

create table TipoProducto (
	codigoTipoProducto int not null,
	descripcion varchar (45) not null,
	primary key Pk_codigoTipoProducto (codigoTipoProducto)
);

create table CargoEmpleado (
	codigoCargoEmpleado int not null,
	nombreCargo varchar (45) not null,
	descripcionCargo varchar (45) not null,
	primary key PK_codigoCargoEmpleado (codigoCargoEmpleado)
);
-- _________________________________________________

create table Productos (
	codigoProducto varchar (15) not null,
	descripcionProducto varchar (45) not null,
	precioUnitario decimal (10,2) not null,
	precioDocena decimal (10,2) not null,
	precioMayor decimal (10,2) not null,
	imagenProducto varchar (15) not null,
    existencia int not null,
	codigoProveedor int not null,
	codigoTipoProducto int not null,
	primary key PK_codigoProducto (codigoProducto),
	constraint FK_Proveedores_Productos foreign key (codigoProveedor)
	references Proveedores (codigoProveedor),
	constraint FK_TipoProducto_Productos foreign key (codigoTipoProducto)
	references TipoProducto (codigoTipoProducto)
);


create table DetalleCompra (
	codigoDetalleCompra int not null,
	costoUnitario decimal (10,2) not null,
    cantidad int not null,
	codigoProducto int not null,
	numeroDocumento int not null,
	primary key PK_codigoDetalleCompra (codigoDetalleCompra),
    constraint FK_Productos_DetalleCompra foreign key (codigoProducto)
    references Productos (codigoProducto),
    constraint FK_Compras_DetalleCompra foreign key (numeroDocumento)
    references Compras (numeroDocumento)
);

create table TelefonoProveedor (
	codigoTelefonoProveedor int not null,
	numeroPrincipal varchar (8) not null,
	numeroSecundario varchar (8) not null,
	observaciones varchar (45) not null,
	codigoProveedor int not null,
	primary key PK_codigoTelefonoProveedor (codigoTelefonoProveedor),
	constraint FK_Proveedores_TelefonoProveedor foreign key (codigoProveedor)
	references Proveedores (codigoProveedor)	
);

create table EmailProveedor (
	codigoEmailProveedor int not null,
	emailProveedor varchar (50) not null,
	descripcion varchar (100) not null,
	codigoProveedor int not null,
	primary key PK_codigoEmailProveedor (codigoEmailProveedor),
	constraint FK_Proveedores_EmailProveedor foreign key (codigoProveedor)
	references Proveedores (codigoProveedor)
);

create table Empleados (
	codigoEmpleado int not null,
	nombreEmpleado varchar (30) not null,
	apellidoEmpleado varchar (30) not null,
	sueldo decimal (10,2),
	direccion varchar (150),
	turno varchar (15),
	codigoCargoEmpleado int not null,
	primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_CargoEmpleado_Empleados foreign key (codigoCargoEmpleado)
	references CargoEmpleado (codigoCargoEmpleado)
);


create table Factura (
	numeroFactura int not null,
    estado varchar (50) not null,
    totalFactura decimal (10,2) not null,
	fechaFactura varchar (45),
	codigoCliente int not null,
	codigoEmpleado int not null,
	total decimal (10,2),
	primary key PK_numeroFactura (numeroFactura),
	constraint FK_Clientes_Factura foreign key (codigoCliente)
	references Clientes (codigoCliente),
	constraint FK_Empleados_Factura foreign key (codigoEmpleado)
	references Empleados (codigoEmpleado)
);

create table DetalleFactura (
	codigoDetalleFactura int not null,
    precioUnitario decimal (10,2) not null,
    cantidad int not null,
	numeroFactura int not null,
	codigoProducto int not null,
	primary key PK_codigoDetalleFactura (codigoDetalleFactura),
	constraint FK_Factura_DetalleFactura foreign key (numeroFactura)
	references Factura (numeroFactura),
	constraint FK_Productos_DetalleFactura foreign key (codigoProducto)
	references Productos (codigoProducto)
);


-- ##########################################################Procedimiento Almacenados ###########################################################
-- ///////////////////////////////////////////////////////////////Clientes////////////////////////////////////////////////////////////////////////
-- ***********************************************************Agregar Clientes********************************************************************
Delimiter $$
	create procedure sp_AgregarClientes (in codigoCliente int , NITCliente varchar(10), in nombreCliente varchar (50), in apellidoCliente varchar (50),
    in direccionCliente varchar (150),in telefonoCliente varchar(8), in correoCliente varchar (45)) 
		Begin 
			Insert into Clientes(codigoCliente, NITCliente, nombreCliente, apellidoCliente,direccionCliente,
            telefonoCliente, correoCliente) values 
            (codigoCliente, NITCliente, nombreCliente, apellidoCliente,direccionCliente,
            telefonoCliente, correoCliente);
            
		End $$
Delimiter ;

call sp_AgregarClientes(01, '11123344', 'Harol', 'Luna', 'San Raymundo', '4355766', 'harolyLuna@gmail.com');
call sp_AgregarClientes(08, '45472378', 'Oliver', 'Donis', 'Amatitlan', '98785675', 'oliveryDonis@gmail.com');
call sp_AgregarClientes(20, '12123020', 'Saul', 'Alberto', 'Casa', '25150641', 'saulyalberto@gmail.com');

-- **********************************************************Listar Clientes*********************************************************************
Delimiter $$
	create procedure sp_ListarClientes()
		Begin
			select 
            C.codigoCliente,
            C.NITCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C;
		End$$
Delimiter ;

call sp_ListarClientes();

-- **********************************************************Buscar Clientes*********************************************************************

Delimiter $$
	create procedure sp_BuscarClientes (in codCli int)
		Begin
			Select 
				C.codigoCliente,
				C.NITCliente,
				C.nombreCliente,
				C.apellidoCliente,
				C.direccionCliente,
				C.telefonoCliente,
				C.correoCliente
            From Clientes C
            where codigoCliente = codCli;
		End$$
Delimiter ;

call sp_BuscarClientes(20);

-- **********************************************************Eliminar Clientes*********************************************************************
Delimiter $$
	create procedure sp_EliminarClientes (in codCli int)
		Begin
			Delete from Clientes
				where codigoCliente = codCli;
        End $$
Delimiter ;

call sp_EliminarClientes(20);
call sp_ListarClientes;

-- **********************************************************Editar Clientes*********************************************************************
Delimiter $$
	create procedure sp_EditarClientes (in codCli int, in nCliente varchar (10), in nomCliente varchar (50), in apCliente varchar (50),
    in direCliente varchar(150), in telCliente varchar (8), in corrCliente varchar (45))
		Begin
			Update Clientes C
					set
			C.NITcliente = nCliente,
            C.nombreCliente = nomCliente,
            C.apellidoCliente = apCliente,
            C.direccionCliente = direCliente,
            C.telefonoCliente = telCliente,
            C.correoCliente = corrCliente
            where codigoCliente = codCli;
		End $$
Delimiter ;

call sp_EditarClientes(08, '97123654', 'Williams', 'Johan', 'Zona 18', '56787654', 'williamsyjohan@gmail.com');
call sp_ListarClientes;
-- ***********************************************************************************************************************************************


-- ////////////////////////////////////////////////////////////Proveedores////////////////////////////////////////////////////////////////////////
-- ********************************************************Agregar Proveedores*********************************************************************
Delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int, in NITProveedor varchar(10), in nombreProveedor varchar(50),
    in apellidoProveedor varchar(50), in direccionProveedor varchar(60), in razonSocial varchar(60), in contactoPrincipal varchar
    (100), in paginaWeb varchar(50))
		Begin 
			 Insert into Proveedores (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, 
             razonSocial , contactoPrincipal, paginaWeb )values
             (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial , 
             contactoPrincipal, paginaWeb );
		End $$
Delimiter ;
 
call sp_AgregarProveedores(1,'124585003','Alexander','Sales','San Pedro','Sony','21549832','AlexanderS_gt');
call sp_AgregarProveedores(2,'124581250','Kellyo','Tasha','Estados Unidos','Bando Estado Unidence','45872100','Tasha.com.gt');
call sp_AgregarProveedores(3,'124581250','Andres','Tasha','Estados Unidos','Bando Estado Unidence','45872100','andres.com.gt');


-- ****************************************************************Listar Proveedores*************************************************************
Delimiter $$
	create procedure sp_ListarProveedores()
		Begin 
			select
            P.codigoProveedor,
			P.NITProveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial ,
			P.contactoPrincipal, 
			P.paginaWeb
			from Proveedores P;
		End $$
Delimiter ;
 
call sp_ListarProveedores();
-- ****************************************************************Buscar Proveedores*************************************************************
Delimiter $$
	create procedure sp_BuscarProveedores(in prID int)
		Begin 
			select
			P.codigoProveedor,
			P.NITProveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial ,
			P.contactoPrincipal, 
			P.paginaWeb
			from Proveedores P
			where codigoProveedor = prID;
		End $$
Delimiter ;
call sp_BuscarProveedores(1);
 
-- **********************************************************Eliminar Proveedores*****************************************************************        
Delimiter $$
	create procedure sp_EliminarProveedores(in prCo int)
		Begin 
			delete from Proveedores
				where codigoProveedor = prCo;
		End $$
Delimiter ;
 
call sp_EliminarProveedores(3);
call sp_ListarProveedores(); 

-- ***********************************************************Editar Proveedores*****************************************************************
Delimiter $$
	create procedure sp_EditarProveedores(in codiProve int, in NProv varchar(10), in nomProve varchar(50),
    in apeProve varchar(50), in direcProve varchar(60), in raSocial varchar(60), in contacPrin varchar
    (100), in pagWeb varchar(50))
		Begin 
			update Proveedores P
				set
			P.NITProveedor = NProv,
			P.nombreProveedor = nomProve,
			P.apellidoProveedor = apeProve,
			P.direccionProveedor = direcProve,
			P.razonSocial = raSocial,
			P.contactoPrincipal = contacPrin, 
			P.paginaWeb = pagWeb
            where codigoProveedor = codiProve;
		End $$
Delimiter ;
 
call sp_EditarProveedores (2,'54123208','Thomas','Sanchez','Acienda Las Flores', 'Bando Estado', '54872100','DeliThomas.gt');
call sp_ListarProveedores(); 

-- ***********************************************************************************************************************************************


-- ///////////////////////////////////////////////////////////////Compras/////////////////////////////////////////////////////////////////////////
-- ***********************************************************Agregar Compras*********************************************************************
Delimiter $$
	create procedure sp_AgregarCompras (in numeroDocumento int ,in fechaDocumento date, in descripcion varchar (60), in totalDocumento decimal (10,2)) 
		Begin 
			Insert into Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento) values 
            (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
            
		End $$
Delimiter ;

call sp_AgregarCompras(0124, '2024-02-12', 'Verdura fresca ', 20.99);
call sp_AgregarCompras(0824, '2024-03-24', 'Frutas frescas', 15.99);
call sp_AgregarCompras(2024, '2024-04-14', 'Carne fresca', 25.99);

-- ****************************************************************Listar Compras*************************************************************
Delimiter $$
	create procedure sp_ListarCompras()
		Begin 
			select
            CP.numeroDocumento,
			CP.fechaDocumento,
			CP.descripcion,
			CP.totalDocumento
			from Compras CP;
		End $$
Delimiter ;
 
call sp_ListarCompras();

-- ****************************************************************Buscar Compras*************************************************************
Delimiter $$
	create procedure sp_BuscarCompras(in numDoc int)
		Begin 
			select
            CP.numeroDocumento,
			CP.fechaDocumento,
			CP.descripcion,
			CP.totalDocumento
			from Compras CP
			where numeroDocumento = numDoc;
		End $$
Delimiter ;
call sp_BuscarCompras(0824);

-- ****************************************************************Eliminar Compras***************************************************************
Delimiter $$
	create procedure sp_EliminarCompras(in numDoc int)
		Begin 
			delete from Compras
				where numeroDocumento = numDoc;
		End $$
Delimiter ;
 
call sp_EliminarCompras(2024);
call sp_ListarCompras(); 

-- *****************************************************************Editar Compras****************************************************************
Delimiter $$
	create procedure sp_EditarCompras(in nuDocu int, in fechDoc date, in descrip varchar(60), in totDoc decimal (10,2))
		Begin 
			update Compras CP
				set
			CP.fechaDocumento = fechDoc,
			CP.descripcion = descrip,
			CP.totalDocumento = totDoc
            where numeroDocumento = nuDocu;
		End $$
Delimiter ;
 
call sp_EditarCompras(0124, '2024-02-20', 'Verdura fresca exportada desde el valle ', 25.99);
call sp_ListarCompras(); 
-- ***********************************************************************************************************************************************


-- ///////////////////////////////////////////////////////////////TipoProducto////////////////////////////////////////////////////////////////////
-- ***********************************************************Agregar TipoProducto******************************************************************    
Delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int , in descripcion varchar (45)) 
		Begin 
			Insert into TipoProducto(codigoTipoProducto, descripcion) values 
            (codigoTipoProducto, descripcion);
		End $$
Delimiter ;

call sp_AgregarTipoProducto(00010, 'Bolsa de verduras frescas  de 500kg');
call sp_AgregarTipoProducto(00020, 'Bolsa de frutas frescas  de 300kg');
call sp_AgregarTipoProducto(00030, 'Bolsa de carne frescas  de 200kg');

-- ****************************************************************Listar Compras*************************************************************
Delimiter $$
	create procedure sp_ListarTipoProducto()
		Begin 
			select
            TP.codigoTipoProducto,
			TP.descripcion
			from TipoProducto TP;
		End $$
Delimiter ;
 
call sp_ListarTipoProducto();

-- ****************************************************************Buscar TipoProducto*************************************************************
Delimiter $$
	create procedure sp_BuscarTipoProducto(in codTiPro int)
		Begin 
			select
            TP.codigoTipoProducto,
			TP.descripcion
			from TipoProducto TP
			where codigoTipoProducto = codTiPro;
		End $$
Delimiter ;
call sp_BuscarTipoProducto(00010);

-- ****************************************************************Eliminar TipoProducto***************************************************************
Delimiter $$
	create procedure sp_EliminarTipoProducto(in codTiPro int)
		Begin 
			delete from TipoProducto 
			where codigoTipoProducto = codTiPro;
		End $$
Delimiter ;
 
call sp_EliminarTipoProducto(00030);
call sp_ListarTipoProducto(); 

-- *****************************************************************Editar TipoProducto****************************************************************
Delimiter $$
	create procedure sp_EditarTipoProducto(in codTiPro int, in descrip varchar(45))
		Begin 
			update TipoProducto TP
				set
			TP.descripcion = descrip
            where codigoTipoProducto = codTiPro;
		End $$
Delimiter ;
 
call sp_EditarTipoProducto(00020, 'Bandeja de frutas frescas  de 100kg');
call sp_ListarTipoProducto(); 
-- ***********************************************************************************************************************************************


-- ///////////////////////////////////////////////////////////////CargoEmpleado////////////////////////////////////////////////////////////////////
-- ***********************************************************Agregar CargoEmpleado******************************************************************    

Delimiter $$
	create procedure sp_AgregarCargoEmpleado (in codigoCargoEmpleado int , in nombreCargo varchar (45), descripcionCargo varchar (45)) 
		Begin 
			Insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo) values 
            (codigoCargoEmpleado, nombreCargo, descripcionCargo);
            
		End $$
Delimiter ;

call sp_AgregarCargoEmpleado(250124, 'Gerente', 'Lider ');
call sp_AgregarCargoEmpleado(080224, 'Subgerente', 'Encargado de coordinar a los trabajadores');
call sp_AgregarCargoEmpleado(200324, 'Empleado', 'Trabajador');

-- ***********************************************************Listar CargoEmpleado******************************************************************    
Delimiter $$
	create procedure sp_ListarCargoEmpleado()
		Begin 
			select
            CE.codigoCargoEmpleado,
			CE.nombreCargo,
            Ce.descripcionCargo
			from CargoEmpleado CE;
		End $$
Delimiter ;
 
call sp_ListarCargoEmpleado();
-- ***********************************************************Buscar CargoEmpleado******************************************************************    
Delimiter $$
	create procedure sp_BuscarCargoEmpleado(in codCarEmp int)
		Begin 
			select
            CE.codigoCargoEmpleado,
			CE.nombreCargo,
            CE.descripcionCargo
			from CargoEmpleado CE
			where codigoCargoEmpleado = codCarEmp;
		End $$
Delimiter ;

call sp_BuscarCargoEmpleado(250124);

-- ***********************************************************Eliminar CargoEmpleado******************************************************************    
Delimiter $$
	create procedure sp_EliminarCargoEmpleado(in codCarEmp int)
		Begin 
			delete from CargoEmpleado 
			where codigoCargoEmpleado = codCarEmp;
		End $$
Delimiter ;
 
call sp_EliminarCargoEmpleado(080224);
call sp_ListarCargoEmpleado(); 
-- ***********************************************************Editar CargoEmpleado******************************************************************    
Delimiter $$
	create procedure sp_EditarCargoEmpleado(in codCarEmp int, in nomCarg varchar(45), in descripCarg varchar(45))
		Begin 
			update CargoEmpleado CE
				set
                CE.nombreCargo = nomCarg,
				CE.descripcionCargo = descripCarg
            where codigoCargoEmpleado = codCarEmp;
		End $$
Delimiter ;
 
call sp_EditarCargoEmpleado(200324, 'Subgerente', 'Encargado de coordinar a los trabajadores');
call sp_ListarCargoEmpleado(); 

-- ***********************************************************************************************************************************************


-- ///////////////////////////////////////////////////////////////Productos///////////////////////////////////////////////////////////////////////
-- ************************************************************Agregar Productos******************************************************************
create table Productos (
	codigoProducto varchar (15) not null,
	descripcionProducto varchar (45) not null,
	precioUnitario decimal (10,2) not null,
	precioDocena decimal (10,2) not null,
	precioMayor decimal (10,2) not null,
	imagenProducto varchar (15) not null,
    existencia int not null,
	codigoProveedor int not null,
	codigoTipoProducto int not null,
	primary key PK_codigoProducto (codigoProducto),
	constraint FK_Proveedores_Productos foreign key (codigoProveedor)
	references Proveedores (codigoProveedor),
	constraint FK_TipoProducto_Productos foreign key (codigoTipoProducto)
	references TipoProducto (codigoTipoProducto)
);
Delimiter $$
	create procedure sp_AgregarProductos (in codigoProducto int, in descripcionProducto varchar (45),in precioUnitario decimal (10,2),
    in precioDocena decimal (10,2), in precioMayor decimal (10,2), in imagenProducto varchar(15), in existencia int
    , in codigoProveedor int, in codigoTipoProducto int) 
		Begin 
			Insert into Productos (codigoProducto, descripcionProducto, precioUnitario, precioDocena, 
            precioMayor, imagenProducto, existencia, codigoProveedor, codigoTipoProducto) values 
            (codigoProducto, descripcionProducto, precioUnitario, precioDocena, 
            precioMayor, imagenProducto, existencia, codigoProveedor, codigoTipoProducto);
            
		End $$
Delimiter ;

call sp_AgregarProductos(01, 'Estas manzanas Gala orgánicas de EcoHarvest 
', 13.99, 10.99, 9.99,'', 11,1,00010);
call sp_AgregarProductos(08,'El filete de res Wagyu PrimeCut 
', 15.99, 13.99, 12.99,'', 15, 2,00020);

-- *********************************************************Listar Producto***********************************************************************
Delimiter $$
	create procedure sp_ListarProductos()
		Begin 
			select
            PD.codigoProducto,
			PD.nombreProducto,
			PD.descripcionProducto,
            PD.cantidadStock,
            PD.precioVentaUnitario,
            PD.precioVentaMayor,
            PD.precioCompra,
            PD.imagenProducto,
            PD.codigoProveedor,
            PD.categoriaProductosId	
			from Productos PD;
		End $$
Delimiter ;

 call sp_ListarProductos();
 
-- *********************************************************Buscar Producto***********************************************************************
Delimiter $$
	create procedure sp_BuscarProductos(in proID int)
		Begin 
			select
			PD.nombreProducto,
			PD.descripcionProducto,
            PD.cantidadStock,
            PD.precioVentaUnitario,
            PD.precioVentaMayor,
            PD.precioCompra,
            PD.imagenProducto,
            PD.codigoProveedor,
            PD.categoriaProductosId	
			from Productos PD
			where codigoProducto = proID;
		End $$
Delimiter ;

call sp_BuscarProductos(08);

-- *********************************************************Eliminar Producto***********************************************************************
Delimiter $$
	create procedure sp_EliminarProductos(in proId int)
		Begin
			Delete from Productos
				where codigoProducto = proId;
        End $$
Delimiter ;

call sp_EliminarProductos(01);
call sp_ListarProductos();

-- ************************************************************Editar Producto*******************************************************************
Delimiter $$
	create procedure sp_EditarProductos(in prodId int , in nomProd varchar (50), in descProd varchar (100), in cantStock int,
    in preVenUni decimal (10,2),in preVenMay decimal (10,2), in preCom decimal (10,2), in imagProd blob, in codiProve int, in cateProdId int) 
		Begin 
			update Productos PD
				set
			PD.nombreProducto = nmProd,
			PD.descripcionProducto =  descProd,
            PD.cantidadStock = cantStock ,
            PD.precioVentaUnitario = preVenUni,
            PD.precioVentaMayor = preVenMay ,
            PD.precioCompra = preCom,
            PD.imagenProducto = imagProd, 
            PD.codigoProveedor = codiProv,
            PD.categoriaProductosId = cateProdId	
            where categoriaProductosId = catProId;
		End $$
Delimiter ;

call sp_EditarProductos(08, 'PrimeCut Filete de Res', 'El filete de res Wagyu PrimeCut es una exquisitez culinaria para los paladares más exigentes. 
', 12, 20.99, 15.99, 13.99,'', 2,2);
call sp_ListarProductos();



