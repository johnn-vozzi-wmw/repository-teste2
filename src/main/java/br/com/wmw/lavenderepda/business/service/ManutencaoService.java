package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SQLiteDriver;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.UsuarioLavendere;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioLavendereDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioPdaPdbxDao;
import totalcross.io.File;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ManutencaoService {
	
	private String pathConfigName = "src/main/java/br/com/wmw/lavenderepda/business/conf/";
	private String pathDaoName = "src/main/java/br/com/wmw/lavenderepda/integration/dao/pdbx/";
	private String packageDomainName = "br.com.wmw.lavenderepda.business.domain.";
	private String packageDomainNameFramework = "br.com.wmw.framework.business.domain.";
	private String packageConfigName = "br.com.wmw.lavenderepda.business.conf.";
	private String packageDaoName = "br.com.wmw.lavenderepda.integration.dao.pdbx.";
	private String daosConfigName = "DaosNameConfig";

    private static ManutencaoService instance;

    public static ManutencaoService getInstance() {
        if (instance == null) {
            instance = new ManutencaoService();
        }
        return instance;
    }
    
	public void atualizaEstruturaTabelas() {
		try {
			VmUtil.debug("----- Início atualização das estruturas das tabelas -----");
			createFileDaoNamesConfig();
			updateTodosParametrosSistema(true);
			//--
			Class<?> classeDaosConfig = Class.forName(packageConfigName + daosConfigName);
			Field[] fields = classeDaosConfig.getDeclaredFields();
			for (Field field : fields) {
				try {
					String fileName = field.getName();
					String domainName = fileName.substring(0, fileName.length() - 3);
					if (fileName.endsWith("PdbxDao")) {
						domainName = fileName.substring(0, fileName.length() - 7);
					} else if (fileName.endsWith("DbxDao")) {
						domainName = fileName.substring(0, fileName.length() - 6);
					}
					Class<?> classeDao = Class.forName(packageDaoName + fileName);
					if (Modifier.isAbstract(classeDao.getModifiers())) {
						VmUtil.debug(fileName + " Não analisada: Abstrata");
						continue;
					}
					CrudDbxDao crudDao;
					try {
						crudDao = (CrudDbxDao)classeDao.newInstance();
					} catch (InstantiationException ee) {
					    ee.printStackTrace();
					    VmUtil.debug("ERRO Não foi possível instanciar a classe " + fileName);
						continue;
					}
					if (ValueUtil.isEmpty(crudDao.getTableName())) {
						VmUtil.debug(fileName + " Não analisada: Não tem tabela associado");
						continue;
					}
					atualizaEstruturaTabela(crudDao, domainName);
				} catch (Throwable t) {
					ExceptionUtil.handle(t);
				}
			}
			//Ajusta problemas cores antigas
			if (LavendereConfig.getInstance().version.startsWith("3")) {
				CorSistemaLavendereService.getInstance().deleteAllByExample(new CorSistema());
			}
			//Ajusta problemas com cargas antigas que ainda nao trabalhavam com a tabela USUARIO
			if (UsuarioLavendereService.getInstance().count() == 0) {
				Vector usuariosPda = UsuarioPdaPdbxDao.getInstance().findAll();
				for (int i = 0; i < usuariosPda.size(); i++) {
					UsuarioPda usuarioPda = (UsuarioPda)usuariosPda.items[i];
					try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
							ResultSet rs = st.executeQuery(UsuarioPdaPdbxDao.getInstance().findByRowKeySql(usuarioPda.getRowKey()))) {
						if (rs.next()) {
							UsuarioLavendere usuario = new UsuarioLavendere();
							usuario.cdUsuario = usuarioPda.cdUsuario;
							usuario.cdFuncao = rs.getString("CDFUNCAO");
							usuario.dsSenha = rs.getString("DSSENHA");
							usuario.dsLogin = "UPDATEDBS";
							usuario.nmUsuario = "UPDATEDBS";
							UsuarioLavendereDbxDao.getInstance().insert(usuario);
						}
					}
				}
			}
			VmUtil.debug("----- Fim atualização das estruturas das tabelas -----");
		} catch (Throwable ex) {
			VmUtil.debug("ERRO ao atualizar tabelas: " + ex.getMessage());
		} finally {
			updateTodosParametrosSistema(false);
		}
	}
	
	private void createFileDaoNamesConfig() {
		if (VmUtil.isJava()) {
			try {
				String[] filesClassesDao = File.listFiles(pathDaoName);
				String filePath = pathConfigName + daosConfigName + ".java";
				FileUtil.deleteFile(filePath);
				File fileDaosNameConfig = FileUtil.criaFile(filePath);
				fileDaosNameConfig.writeBytes("package br.com.wmw.lavenderepda.business.conf; \r\npublic class DaosNameConfig { \r\n\r\n");
				for (String string : filesClassesDao) {
					File f = FileUtil.openFile(string, File.DONT_OPEN);
					if (f.isDir()) {
						continue;
					}
					String fileName = f.getPath().substring(pathDaoName.length(), f.getPath().length() - 5);
					fileDaosNameConfig.writeBytes("\tpublic String " + fileName + ";\r\n");
				}
				fileDaosNameConfig.writeBytes("}");
				FileUtil.closeFile(fileDaosNameConfig);
			} catch (Throwable ex) {
				VmUtil.debug("ERRO !!!ATENÇÃO!!! erro ao gerar arquivo DaosNameConfig.java: " + ex.getMessage());
			}
		}
	}
	
	private void atualizaEstruturaTabela(CrudDbxDao crudDao, String domainName) throws SQLException {
	   // faz um select em cada campo da tabela pra ver se o campo existe
	   String table = crudDao.getTableName();
		StringBuffer sb = new StringBuffer();
		crudDao.addSelectColumns(null, sb);
		String[] campos = sb.toString().split(",");
		for (String s : campos) {
			String campo = s.trim();
			if (campo.contains(".")) {
				String tablePrefix = campo.substring(0, campo.indexOf('.'));
				if (!tablePrefix.equalsIgnoreCase("tb")) {
					continue;
				}
				campo = campo.substring(campo.indexOf('.') + 1);
			}
			try {
				crudDao.findColumnByRowKey("X", campo);
			} catch (SQLException ex) {
				if (SQLiteDriver.isNoSuchColumn(ex))
					adicionaColuna(crudDao, campo, domainName);
				else if (SQLiteDriver.isNoSuchTable(ex))
					criaTabela(crudDao, campos, domainName);
				else {
					VmUtil.debug("ERRO desconhecido na análise " + domainName + " " + ex.getMessage());
				}
			} catch (Throwable e) {
				VmUtil.debug("ERRO desconhecido na análise " + domainName + " " + e.getMessage());
			}
		}
		//--
		if (table.equalsIgnoreCase(Pedido.TABLE_NAME_PEDIDO) || table.equalsIgnoreCase(ItemPedido.TABLE_NAME_ITEMPEDIDO) ||
						table.equalsIgnoreCase(ItemPedidoGrade.TABLE_NAME_ITEMPEDIDOGRADE) || table.equalsIgnoreCase(ItemKitPedido.TABLE_NAME_ITEMKITPEDIDO)) {
			crudDao.setTable(table + "ERP");
			atualizaEstruturaTabela(crudDao, domainName);
		}
	}
	
	private void updateTodosParametrosSistema(boolean flLigaParametro) {
		try {
			Class<?> pdaConfigClass = LavenderePdaConfig.class;
			Field[] fields = pdaConfigClass.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isFinal(field.getModifiers())) {
					try {
						if (field.getType().getName().equalsIgnoreCase("int") || field.getType().getName().equalsIgnoreCase(Integer.class.getName())) {
							field.set(pdaConfigClass, flLigaParametro ? 1 : 0);
						} else if (field.getType().getName().equalsIgnoreCase("double") || field.getType().getName().equalsIgnoreCase(Double.class.getName())) {
							field.set(pdaConfigClass, flLigaParametro ? 1 : 0);
						} else if ((field.getType().getName().equalsIgnoreCase("boolean") || field.getType().getName().equalsIgnoreCase(Boolean.class.getName())) && (!field.getName().contains("oculta") && !field.getName().contains("Oculta") && !field.getName().contains("oculto") && !field.getName().contains("Oculto"))) {
							field.set(pdaConfigClass, flLigaParametro);
						} else if (field.getType().getName().equalsIgnoreCase(String.class.getName())) {
							field.set(pdaConfigClass, flLigaParametro ? "S" : "N");
						} 
					} catch (Throwable e) {
						VmUtil.debug("ERRO Não foi possível ligar o parâmetro " + field.getName() + " : " + e.getMessage());
					}
				}
			}
		} catch (Throwable ex) {
			VmUtil.debug("ERRO Não foi possível ligar os parâmetros: " + ex.getMessage());
		}
	}
	
	private void criaTabela(CrudDbxDao crudDao, String[] campos, String domainName) {
		StringBuffer sbCreateTable = new StringBuffer();
		sbCreateTable.append("CREATE TABLE ");
		sbCreateTable.append(crudDao.getTableName());
		sbCreateTable.append("(");
		//--
		Class<?> classeDomain = getDomainClass(domainName);
		if (classeDomain == null) {
			VmUtil.debug("ERRO TABELA NÃO PÔDE SER CRIADA (Domain não encontrado) " + crudDao.getTableName());
			return;
		}
		Field[] fileds = classeDomain.getFields();
		for (int k = 0; k < fileds.length; k++) {
			Field field = fileds[k];
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			if (field.getName().equalsIgnoreCase("rowKey") || field.getName().equalsIgnoreCase("nuCarimbo")) {
				continue;
			}
			for (int h = 0; h < campos.length; h++) {
				String campo = campos[h].trim();
				if (campo.contains(".")) {
					campo = campo.substring(campo.indexOf('.') + 1);
				}
				if (field.getName().equalsIgnoreCase(campo)) {
					String clasula = field.getName() + " " + getLitebaseTypeFromFiled(field) + ",";
					if (!sbCreateTable.toString().contains(clasula)) {
						sbCreateTable.append(clasula);
					}
				}
			}
		}
		sbCreateTable.append(" nuCarimbo int, rowkey char(200)");
		sbCreateTable.append(")");
		try {
			CrudDbxDao.getCurrentDriver().execute(sbCreateTable.toString());
			VmUtil.debug("TABELA CRIADA " + crudDao.getTableName() + " - COMANDO: " + sbCreateTable.toString());
		} catch (Throwable exx) {
			VmUtil.debug("ERRO TABELA NÃO PÔDE SER CRIADA " + crudDao.getTableName() + " ex:" + exx.getMessage());
		}
	}
	
	private void adicionaColuna(CrudDbxDao crudDao, String campo, String domainName) {
		StringBuffer sbCreateTable = new StringBuffer();
		sbCreateTable.append("ALTER TABLE ");
		sbCreateTable.append(crudDao.getTableName());
		sbCreateTable.append(" ADD ");
		sbCreateTable.append(campo);
		//--
		Class<?> classeDomain = getDomainClass(domainName);
		if (classeDomain == null) {
			VmUtil.debug("ERRO COLUNA NÃO PÔDE SER CRIADA (Domain não encontrado) " + campo + " da " + crudDao.getTableName());
			return;
		}
		Field[] fileds = classeDomain.getFields();
		for (int k = 0; k < fileds.length; k++) {
			Field field = fileds[k];
			if (field.getName().equalsIgnoreCase(campo)) {
				sbCreateTable.append(" " + getLitebaseTypeFromFiled(field));
				break;
			}
		}
		if (sbCreateTable.toString().endsWith(campo)) {
			sbCreateTable.append(" char(200)");
		}
		try {
			CrudDbxDao.getCurrentDriver().executeUpdate(sbCreateTable.toString());
			VmUtil.debug("Coluna Criada " + campo + " na " + crudDao.getTableName() + " - COMANDO: " + sbCreateTable.toString());
		} catch (Throwable exx) {
			VmUtil.debug("ERRO Coluna NÃO pôde ser criada " + campo + " na " + crudDao.getTableName());
		}
	}
	
	private Class<?> getDomainClass(String domainName) {
		try {
			return Class.forName(packageDomainName + domainName);
		} catch (ClassNotFoundException ex) {
			try {
				if (domainName.endsWith("Lavendere")) {
					return Class.forName(packageDomainNameFramework + (domainName.substring(0, domainName.length() - 9)));
				} else {
					return Class.forName(packageDomainNameFramework + domainName);
				}
			} catch (ClassNotFoundException ex2) {
				return null;
			}
		}
	}

	private String getLitebaseTypeFromFiled(Field field) {
		if (field.getType().getName().equalsIgnoreCase("int") || field.getType().getName().equalsIgnoreCase(Integer.class.getName())) {
			return "int";
		} else if (field.getType().getName().equals("double") || field.getType().getName().equalsIgnoreCase(Double.class.getName())) {
			return "double";
		} else if (field.getType().getName().equals("Date") || field.getType().getName().equalsIgnoreCase(Date.class.getName())) {
			return "Date";
		} else {
			return "char(200)";
		}
	}
	
	public Vector getTableNamesFromDaoNamesConfig() {
		try {
			Vector tableList = new Vector();
			Class<?> classeDaosConfig = Class.forName(packageConfigName + daosConfigName);
			Field[] fields = classeDaosConfig.getDeclaredFields();
			for (Field field : fields) {
				Class<?> classeDao = Class.forName(packageDaoName + field.getName());
				if (Modifier.isAbstract(classeDao.getModifiers())) {
					continue;
				}
				CrudDbxDao crudDao;
				try {
					crudDao = (CrudDbxDao)classeDao.newInstance();
				} catch (InstantiationException ee) {
					continue;
				}
				if (ValueUtil.isEmpty(crudDao.getTableName())) {
					continue;
				}
				tableList.addElement(crudDao.getTableName());
			}
			return tableList;
		} catch (Throwable ex) {
			return new Vector(0);
		}
	}


}
