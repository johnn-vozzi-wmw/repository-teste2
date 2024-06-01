package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import totalcross.sql.ResultSet;

public class AtendimentoAtivDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new AtendimentoAtiv();
	}

    private static AtendimentoAtivDao instance;

    public AtendimentoAtivDao() {
        super(AtendimentoAtiv.TABLE_NAME);
    }
    
    public static AtendimentoAtivDao getInstance() {
        if (instance == null) {
            instance = new AtendimentoAtivDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        AtendimentoAtiv atendimentoAtiv = new AtendimentoAtiv();
        atendimentoAtiv.rowKey = rs.getString("rowkey");
        atendimentoAtiv.cdEmpresa = rs.getString("cdEmpresa");
        atendimentoAtiv.cdRepresentante = rs.getString("cdRepresentante");
        atendimentoAtiv.cdCliente = rs.getString("cdCliente");
        atendimentoAtiv.cdAtendimentoAtividade = rs.getString("cdAtendimentoAtividade");
        atendimentoAtiv.cdUsuarioAtendimento = rs.getString("cdUsuarioAtendimento");
        atendimentoAtiv.cdUsuarioOriginal = rs.getString("cdUsuarioOriginal");
        atendimentoAtiv.cdAtendimentoGeracao = rs.getString("cdAtendimentoGeracao");
        atendimentoAtiv.cdSac = rs.getString("cdSac");
        atendimentoAtiv.cdTipoSac = rs.getString("cdTipoSac");
        atendimentoAtiv.cdAtividadeSac = rs.getString("cdAtividadeSac");
        atendimentoAtiv.cdPesquisa = rs.getString("cdPesquisa");
        atendimentoAtiv.dsObservacao = rs.getString("dsObservacao");
        atendimentoAtiv.dtAtendimento = rs.getDate("dtAtendimento");
        atendimentoAtiv.dtAlteracao = rs.getDate("dtAlteracao");
        atendimentoAtiv.flTipoGeracao = rs.getString("flTipoGeracao");
        atendimentoAtiv.flTipoAtendimento = rs.getString("flTipoAtendimento");
        atendimentoAtiv.cdStatusAtendimento = rs.getString("cdStatusAtendimento");
        atendimentoAtiv.flTipoAlteracao = rs.getString("flTipoAlteracao");
        atendimentoAtiv.nuCarimbo = rs.getInt("nuCarimbo");
        atendimentoAtiv.hrAtendimento = rs.getString("hrAtendimento");
        atendimentoAtiv.hrAlteracao = rs.getString("hrAlteracao");
        atendimentoAtiv.flOrigem = rs.getString("flOrigem");
        atendimentoAtiv.cdUsuario = rs.getString("cdUsuario");
        return atendimentoAtiv;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDATENDIMENTOATIVIDADE,");
        sql.append(" CDUSUARIOATENDIMENTO,");
        sql.append(" CDUSUARIOORIGINAL,");
        sql.append(" CDATENDIMENTOGERACAO,");
        sql.append(" CDSAC,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDATIVIDADESAC,");
        sql.append(" CDPESQUISA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTATENDIMENTO,");
        sql.append(" FLTIPOGERACAO,");
        sql.append(" FLTIPOATENDIMENTO,");
        sql.append(" CDSTATUSATENDIMENTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" HRATENDIMENTO,");
        sql.append(" FLORIGEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDATENDIMENTOATIVIDADE,");
        sql.append(" CDUSUARIOATENDIMENTO,");
        sql.append(" CDUSUARIOORIGINAL,");
        sql.append(" CDATENDIMENTOGERACAO,");
        sql.append(" CDSAC,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDATIVIDADESAC,");
        sql.append(" CDPESQUISA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" DTATENDIMENTO,");
        sql.append(" FLTIPOGERACAO,");
        sql.append(" FLTIPOATENDIMENTO,");
        sql.append(" CDSTATUSATENDIMENTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" HRATENDIMENTO,");
        sql.append(" FLORIGEM,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) domain;
        sql.append(Sql.getValue(atendimentoAtiv.cdEmpresa)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdRepresentante)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdCliente)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdAtendimentoAtividade)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdUsuarioAtendimento)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdUsuarioOriginal)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdAtendimentoGeracao)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdSac)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdTipoSac)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdAtividadeSac)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdPesquisa)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.dsObservacao)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.dtAtendimento)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.flTipoGeracao)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.flTipoAtendimento)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdStatusAtendimento)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.nuCarimbo)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.hrAtendimento)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.flOrigem)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.cdUsuario)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.dtAlteracao)).append(",");
        sql.append(Sql.getValue(atendimentoAtiv.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) domain;
        sql.append(" CDUSUARIOATENDIMENTO = ").append(Sql.getValue(atendimentoAtiv.cdUsuarioAtendimento)).append(",");
        sql.append(" CDUSUARIOORIGINAL = ").append(Sql.getValue(atendimentoAtiv.cdUsuarioOriginal)).append(",");
        sql.append(" CDATENDIMENTOGERACAO = ").append(Sql.getValue(atendimentoAtiv.cdAtendimentoGeracao)).append(",");
        sql.append(" CDSAC = ").append(Sql.getValue(atendimentoAtiv.cdSac)).append(",");
        sql.append(" CDTIPOSAC = ").append(Sql.getValue(atendimentoAtiv.cdTipoSac)).append(",");
        sql.append(" CDATIVIDADESAC = ").append(Sql.getValue(atendimentoAtiv.cdAtividadeSac)).append(",");
        sql.append(" CDPESQUISA = ").append(Sql.getValue(atendimentoAtiv.cdPesquisa)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(atendimentoAtiv.dsObservacao)).append(",");
        sql.append(" DTATENDIMENTO = ").append(Sql.getValue(atendimentoAtiv.dtAtendimento)).append(",");
        sql.append(" FLTIPOGERACAO = ").append(Sql.getValue(atendimentoAtiv.flTipoGeracao)).append(",");
        sql.append(" FLTIPOATENDIMENTO = ").append(Sql.getValue(atendimentoAtiv.flTipoAtendimento)).append(",");
        sql.append(" CDSTATUSATENDIMENTO = ").append(Sql.getValue(atendimentoAtiv.cdStatusAtendimento)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(atendimentoAtiv.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(atendimentoAtiv.nuCarimbo)).append(",");
        sql.append(" HRATENDIMENTO = ").append(Sql.getValue(atendimentoAtiv.hrAtendimento)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(atendimentoAtiv.cdUsuario)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(atendimentoAtiv.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(atendimentoAtiv.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", atendimentoAtiv.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", atendimentoAtiv.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", atendimentoAtiv.cdCliente);
		sqlWhereClause.addAndCondition("CDATENDIMENTOATIVIDADE = ", atendimentoAtiv.cdAtendimentoAtividade);
		sqlWhereClause.addAndCondition("CDSAC = ", atendimentoAtiv.cdSac);
		sqlWhereClause.addAndCondition("CDSTATUSATENDIMENTO = ", atendimentoAtiv.cdStatusAtendimento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}