package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class ContatoErpPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContatoErp();
	}

    private static ContatoErpPdbxDao instance;

    public ContatoErpPdbxDao() {
        super(ContatoErp.TABLE_NAME);
    }
    
    public String getTableNameToCamposDinamicos() {
    	return Contato.TABLE_NAME;
    }

    public static ContatoErpPdbxDao getInstance() {
        if (instance == null) {
            instance = new ContatoErpPdbxDao();
        }
        return instance;
    }

    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
    	ContatoErp contato = (ContatoErp) domain;
    	addUpdateValuesPerson(contato, sql);
    }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		//Faz Nada
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONTATO,");
        sql.append(" NMCONTATO,");
        sql.append(" NUFONE,");
        sql.append(" NUCELULAR,");
        sql.append(" DSCARGO,");
        sql.append(" FLSEXO,");
        sql.append(" DSEMAIL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTANIVERSARIO,");
        sql.append(" CDCONTATORELACIONADO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" cdUsuario,");
        sql.append(" FLDEFAULT,");
		sql.append(" FLCONTATONOVOCLIENTE");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ContatoErp contatoerp = new ContatoErp();
        contatoerp.rowKey = rs.getString("rowkey");
        contatoerp.cdEmpresa = rs.getString("cdEmpresa");
        contatoerp.cdRepresentante = rs.getString("cdRepresentante");
        contatoerp.cdCliente = rs.getString("cdCliente");
        contatoerp.cdContato = rs.getString("cdContato");
        contatoerp.nmContato = rs.getString("nmContato");
        contatoerp.nuFone = rs.getString("nuFone");
        contatoerp.nuCelular = rs.getString("nuCelular");
        contatoerp.dsCargo = rs.getString("dsCargo");
        contatoerp.flSexo = rs.getString("flSexo");
        contatoerp.dsEmail = rs.getString("dsEmail");
        contatoerp.dsObservacao = rs.getString("dsObservacao");
        contatoerp.dtAniversario = rs.getDate("dtAniversario");
        contatoerp.cdContatoRelacionado = rs.getString("cdContatorelacionado");
        contatoerp.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contatoerp.cdUsuario = rs.getString("cdUsuario");
        contatoerp.flDefault = rs.getString("flDefault");
		contatoerp.flContatoNovoCliente = rs.getString("flContatoNovoCliente");
        return contatoerp;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContatoErp contatoerp = (ContatoErp) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contatoerp.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contatoerp.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contatoerp.cdCliente);
		sqlWhereClause.addAndCondition("CDCONTATO = ", contatoerp.cdContato);
		sqlWhereClause.addAndCondition("CDCONTATORELACIONADO = ", contatoerp.cdContatoRelacionado);
		sqlWhereClause.addAndCondition("FLDEFAULT = ", contatoerp.flDefault);
		sqlWhereClause.addAndCondition("FLCONTATONOVOCLIENTE = ", contatoerp.flContatoNovoCliente);
		sql.append(sqlWhereClause.getSql());
		//--
		if (!ValueUtil.isEmpty(contatoerp.dtAniversario)) {
			contatoerp.dtAniversario.advance(contatoerp.dtAniversario.getDayOfWeek() * -1);
			int mesInicial = contatoerp.dtAniversario.getMonth();
			int diaInicial = contatoerp.dtAniversario.getDay();
			//--
			contatoerp.dtAniversario.advance(6 - contatoerp.dtAniversario.getDayOfWeek());
			int mesFinal = contatoerp.dtAniversario.getMonth();
			int diaFinal = contatoerp.dtAniversario.getDay();
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
    	sql.append(" select tb.CDEMPRESA, tb.CDCLIENTE, tb.CDCONTATO, tb.NMCONTATO, tb.NUFONE, tb.NUCELULAR, tb.DSCARGO, tb.FLSEXO, tb.DSEMAIL, "
    			+ "tb.DSOBSERVACAO, tb.DTANIVERSARIO, tb.CDCONTATORELACIONADO, tb.CDUSUARIO");
    	sql.append(" from ");
    	sql.append(tableName);
    	sql.append(" tb ");
    	addWhereByExample(domain, sql);
    	sql.append(" group by tb.CDEMPRESA, tb.CDCLIENTE, tb.CDCONTATO, tb.NMCONTATO, tb.NUFONE, tb.NUCELULAR, tb.DSCARGO, tb.FLSEXO, tb.DSEMAIL, "
    			+ "tb.DSOBSERVACAO, tb.DTANIVERSARIO, tb.CDCONTATORELACIONADO, tb.CDUSUARIO ");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector result = new Vector(50);
    		while (rs.next()) {
    			ContatoErp contatoerp = new ContatoErp();
    			contatoerp.cdEmpresa = rs.getString("cdEmpresa");
    			contatoerp.cdCliente = rs.getString("cdCliente");
    			contatoerp.cdContato = rs.getString("cdContato");
    			contatoerp.nmContato = rs.getString("nmContato");
    			contatoerp.nuFone = rs.getString("nuFone");
    			contatoerp.nuCelular = rs.getString("nuCelular");
    			contatoerp.dsCargo = rs.getString("dsCargo");
    			contatoerp.flSexo = rs.getString("flSexo");
    			contatoerp.dsEmail = rs.getString("dsEmail");
    			contatoerp.dsObservacao = rs.getString("dsObservacao");
    			contatoerp.dtAniversario = rs.getDate("dtAniversario");
    			contatoerp.cdContatoRelacionado = rs.getString("cdContatorelacionado");
    			contatoerp.cdUsuario = rs.getString("cdUsuario");
    			result.addElement(contatoerp);
    		}
    		return result;
    	}
    }
}