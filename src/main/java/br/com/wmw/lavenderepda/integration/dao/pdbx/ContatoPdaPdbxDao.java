package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.ContatoPda;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ContatoPdaPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContatoPda();
	}

    private static ContatoPdaPdbxDao instance;

    public ContatoPdaPdbxDao() {
        super(Contato.TABLE_NAME);
    }

    public static ContatoPdaPdbxDao getInstance() {
        if (instance == null) {
            instance = new ContatoPdaPdbxDao();
        }
        return instance;
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		// Faz nada
	}

	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ContatoPda contato = new ContatoPda();
        contato.rowKey = rs.getString("rowkey");
        contato.cdEmpresa = rs.getString("cdEmpresa");
        contato.cdRepresentante = rs.getString("cdRepresentante");
        contato.cdCliente = rs.getString("cdCliente");
        contato.flOrigemContato = rs.getString("flOrigemcontato");
        contato.flAcaoAlteracao = rs.getString("flAcaoAlteracao");
        contato.cdContato = rs.getString("cdContato");
        contato.cdRegistro = rs.getString("cdRegistro");
        contato.nmContato = rs.getString("nmContato");
        contato.nuFone = rs.getString("nuFone");
        contato.nuCelular = rs.getString("nuCelular");
        contato.dsCargo = rs.getString("dsCargo");
        contato.flSexo = rs.getString("flSexo");
        contato.dsEmail = rs.getString("dsEmail");
        contato.dsObservacao = rs.getString("dsObservacao");
        contato.dtAniversario = rs.getDate("dtAniversario");
        contato.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contato.cdUsuario = rs.getString("cdUsuario");
        contato.flContatoNovoCliente = rs.getString("flContatoNovoCliente");
        return contato;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLORIGEMCONTATO,");
        sql.append(" FLACAOALTERACAO,");
        sql.append(" CDCONTATO,");
        sql.append(" CDREGISTRO,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" NUCELULAR,");
        sql.append(" DSCARGO,");
        sql.append(" FLSEXO,");
        sql.append(" DSEMAIL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTANIVERSARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
		sql.append(" FLCONTATONOVOCLIENTE");
    }

    protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
        ContatoPda contato = (ContatoPda) domain;
        //--
		if (columnName.equalsIgnoreCase("ROWKEY")) {
			return Sql.getValue(contato.getRowKey());
		}
		if (columnName.equalsIgnoreCase("CDEMPRESA")) {
			return Sql.getValue(contato.cdEmpresa);
		}
		if (columnName.equalsIgnoreCase("CDREPRESENTANTE")) {
			return Sql.getValue(contato.cdRepresentante);
		}
		if (columnName.equalsIgnoreCase("CDCLIENTE")) {
			return Sql.getValue(contato.cdCliente);
		}
		if (columnName.equalsIgnoreCase("FLACAOALTERACAO")) {
			return Sql.getValue(contato.flAcaoAlteracao);
		}
		if (columnName.equalsIgnoreCase("FLORIGEMCONTATO")) {
			return Sql.getValue(contato.flOrigemContato);
		}
		if (columnName.equalsIgnoreCase("CDCONTATO")) {
			return Sql.getValue(contato.cdContato);
		}
		if (columnName.equalsIgnoreCase("CDREGISTRO")) {
			return Sql.getValue(contato.cdRegistro);
		}
		if (columnName.equalsIgnoreCase("NUCARIMBO")) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if (columnName.equalsIgnoreCase("FLTIPOALTERACAO")) {
			return Sql.getValue(contato.flTipoAlteracao);
		}
		if (columnName.equalsIgnoreCase("CDUSUARIO")) {
			return Sql.getValue(contato.cdUsuario);
		}
		if (columnName.equalsIgnoreCase("FLCONTATONOVOCLIENTE")) {
			return Sql.getValue(contato.flContatoNovoCliente);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
    }
    
    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ContatoPda contato = (ContatoPda) domain;
        addUpdateValuesPerson(contato, sql);
        sql.append(" cdUsuario = ").append(Sql.getValue(contato.cdUsuario)).append(",");
        sql.append(" flContatoNovoCliente = ").append(Sql.getValue(contato.flContatoNovoCliente));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContatoPda contato = (ContatoPda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contato.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contato.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contato.cdCliente);
		sqlWhereClause.addAndCondition("FLORIGEMCONTATO = ", contato.flOrigemContato);
		sqlWhereClause.addAndCondition("FLACAOALTERACAO != ", contato.flAcaoAlteracao);
		sqlWhereClause.addAndCondition("CDCONTATO = ", contato.cdContato);
		sqlWhereClause.addAndCondition("CDREGISTRO = ", contato.cdRegistro);
		sql.append(sqlWhereClause.getSql());
		//--
		if (!ValueUtil.isEmpty(contato.dtAniversario)) {
			contato.dtAniversario.advance(contato.dtAniversario.getDayOfWeek() * -1);
			int mesInicial = contato.dtAniversario.getMonth();
			int diaInicial = contato.dtAniversario.getDay();
			//--
			contato.dtAniversario.advance(6 - contato.dtAniversario.getDayOfWeek());
			int mesFinal = contato.dtAniversario.getMonth();
			int diaFinal = contato.dtAniversario.getDay();
			//--
			sql.append(" and (");
			sql.append(" (strftime('%m', dtAniversario) = ").append(Sql.getSqliteDayMothValue(mesInicial));
			sql.append(" and strftime('%d', dtAniversario) >= ").append(Sql.getSqliteDayMothValue(diaInicial)).append(") ");
			if (mesInicial == mesFinal) {
				sql.append(" and ");
			} else {
				sql.append(" or ");
			}
			sql.append(" (strftime('%m', dtAniversario) = ").append(Sql.getSqliteDayMothValue(mesFinal));
			sql.append(" and strftime('%d', dtAniversario) <= ").append(Sql.getSqliteDayMothValue(diaFinal));
			sql.append("))");
			sql.append(" and strftime('%Y', dtAniversario) != 2999");
		}
    }
    
    public Vector findAllGroupByRepresentante(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select tb.CDEMPRESA, tb.CDCLIENTE, tb.FLORIGEMCONTATO, tb.FLACAOALTERACAO, tb.CDCONTATO, tb.CDREGISTRO, tb.NMCONTATO, tb.NUFONE, tb.NUCELULAR, tb.DSCARGO, tb.FLSEXO, tb.DSEMAIL, "
    			+ "tb.DSOBSERVACAO, tb.DTANIVERSARIO, tb.CDUSUARIO");
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(domain, sql);
    	sql.append(" group by tb.CDEMPRESA, tb.CDCLIENTE, tb.FLORIGEMCONTATO, tb.FLACAOALTERACAO, tb.CDCONTATO, tb.CDREGISTRO, tb.NMCONTATO, tb.NUFONE, tb.NUCELULAR, tb.DSCARGO, tb.FLSEXO, tb.DSEMAIL, "
    			+ "tb.DSOBSERVACAO, tb.DTANIVERSARIO, tb.CDUSUARIO ");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector result = new Vector(50);
    		while (rs.next()) {
    			ContatoPda contato = new ContatoPda();
    			contato.cdEmpresa = rs.getString("cdEmpresa");
    			contato.cdCliente = rs.getString("cdCliente");
    			contato.flOrigemContato = rs.getString("flOrigemcontato");
    			contato.flAcaoAlteracao = rs.getString("flAcaoAlteracao");
    			contato.cdContato = rs.getString("cdContato");
    			contato.cdRegistro = rs.getString("cdRegistro");
    			contato.nmContato = rs.getString("nmContato");
    			contato.nuFone = rs.getString("nuFone");
    			contato.nuCelular = rs.getString("nuCelular");
    			contato.dsCargo = rs.getString("dsCargo");
    			contato.flSexo = rs.getString("flSexo");
    			contato.dsEmail = rs.getString("dsEmail");
    			contato.dsObservacao = rs.getString("dsObservacao");
    			contato.dtAniversario = rs.getDate("dtAniversario");
    			contato.flTipoAlteracao = rs.getString("flTipoAlteracao");
    			contato.cdUsuario = rs.getString("cdUsuario");
				contato.flContatoNovoCliente = rs.getString("flContatoNovoCliente");
    			result.addElement(contato);
    		}
    		return result;
    	}
    }

	public Vector findAllContatoByNovoCliente(String nuCnpj) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT ");
		addSelectColumns(null, sql);
		sql.append(" FROM ");
		sql.append(tableName);
		sql.append(" TB ");
		sql.append(" WHERE CDCLIENTE = ").append(Sql.getValue(nuCnpj));
		return findAll(null, sql.toString());
	}

}