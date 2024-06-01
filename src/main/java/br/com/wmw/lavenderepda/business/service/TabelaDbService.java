package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.TabelaDb;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class TabelaDbService {
	
	private static TabelaDbService instance;
	
	public static TabelaDbService getInstance() {
        if (instance == null) {
            instance = new TabelaDbService();
        }
        return instance;
    }

	public Vector converteTabelaListToTableNameList(Vector tabelaList) {
		Vector tableNameList = new Vector();
		for (int i = 0; i < tabelaList.size(); i++) {
			TabelaDb tabela = (TabelaDb)tabelaList.items[i];
			tableNameList.addElement(tabela.nmTabela);
		}
		return tableNameList;
	}

	/**
	 * Atualiza a lista de tabelas, deixando somente aquelas que existem no tableNameList
	 * @param tabelaList Lista de tabelas
	 * @param tableNameList Lista de nomes de tabelas
	 * @return Lista de tabelas atualizadas
	 */
	public Vector atualizaTabelaListByTableNameList(Vector tabelaList, Vector tableNameList) {
		for (int i = 0; i < tabelaList.size(); i++) {
			boolean encontrou = false;
			TabelaDb tabela = (TabelaDb) tabelaList.items[i];
			for (int j = 0; j < tableNameList.size(); j++) {
				String nmTabela = (String) tableNameList.items[j];
				if (tabela.nmTabela.equals(nmTabela)) {
					encontrou = true;
					break;
				}
			}
			if (!encontrou) {
				tabelaList.removeElement(tabela);
			}
		}
		return tabelaList;
	}
	
	public int getMaxCarimbo(String tableName) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("select max(");
		sql.append(BaseDomain.NMCAMPONUCARIMBO);
		sql.append(") as maxCarimbo from ");
		sql.append(tableName);
		return CrudDbxDao.getCurrentDriver().getInt(sql.toString());
	}

    public Vector getTabelaList() throws SQLException {
    	Vector tabelaList = new Vector();
    	Vector tableNames = SyncManager.getInfoAtualizacao(false).getKeys();
    	if (ValueUtil.isNotEmpty(tableNames)) {
    		int size = tableNames.size();
    		for (int i = 0; i < size; i++) {
    			String nmTabela = (String) tableNames.items[i];
    			computeTableInfo(tabelaList, nmTabela);
			}
    	}
		return tabelaList;
    }

	private void computeTableInfo(Vector tabelaList, String nmTabela) throws SQLException {
		TabelaDb tabela = new TabelaDb();
		tabela.nmTabela = nmTabela;
		try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery("select count(*) qt, max(nucarimbo) from " + nmTabela)) {
			if (rs.next()) {
				tabela.nuRegistros = rs.getInt(1);
				tabela.nuMaiorCarimbo = rs.getInt(2);
				tabelaList.addElement(tabela);
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			try (Statement st = CrudDbxDao.getCurrentDriver().getStatement();
					ResultSet rs = st.executeQuery("select count(*) qt from " + nmTabela)) {
				if (rs.next()) {
					tabela.nuRegistros = rs.getInt(1);
					tabela.nuMaiorCarimbo = 0;
					tabelaList.addElement(tabela);
				}
			} catch (Exception e2) {
				ExceptionUtil.handle(e2);
			}
		}
	}
    
	public void enviaBancoToServidor() throws Exception {
    	LavendereTc2Web pdaToErp = new LavendereTc2Web();
    	pdaToErp.enviaBancoDadosToErp(DatabaseUtil.DIR_BACKUP_DATABASE_SERVIDOR);
    }
    
}
