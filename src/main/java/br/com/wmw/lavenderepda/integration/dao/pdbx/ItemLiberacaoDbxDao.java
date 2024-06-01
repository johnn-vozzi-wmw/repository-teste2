package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ItemLiberacao;
import totalcross.sql.ResultSet;

public class ItemLiberacaoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ItemLiberacao();
	}

    private static ItemLiberacaoDbxDao instance;

    public ItemLiberacaoDbxDao() {
        super(ItemLiberacao.TABLE_NAME);
    }
    
    public static ItemLiberacaoDbxDao getInstance() {
        if (instance == null) {
            instance = new ItemLiberacaoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ItemLiberacao itemLiberacao = new ItemLiberacao();
        itemLiberacao.rowKey = rs.getString("rowkey");
        itemLiberacao.cdEmpresa = rs.getString("cdEmpresa");
        itemLiberacao.cdRepresentante = rs.getString("cdRepresentante");
        itemLiberacao.flOrigemPedido = rs.getString("flOrigemPedido");
        itemLiberacao.nuPedido = rs.getString("nuPedido");
        itemLiberacao.cdUsuarioLiberacao = rs.getString("cdUsuarioLiberacao");
        itemLiberacao.nmUsuario = rs.getString("nmUsuario");
        itemLiberacao.nuCarimbo = rs.getInt("nuCarimbo");
        itemLiberacao.flTipoAlteracao = rs.getString("flTipoAlteracao");
        itemLiberacao.cdUsuario = rs.getString("cdUsuario");
        return itemLiberacao;
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
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDUSUARIOLIBERACAO,");
        sql.append(" NMUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDUSUARIOLIBERACAO,");
        sql.append(" NMUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemLiberacao itemLiberacao = (ItemLiberacao) domain;
        sql.append(Sql.getValue(itemLiberacao.cdEmpresa)).append(",");
        sql.append(Sql.getValue(itemLiberacao.cdRepresentante)).append(",");
        sql.append(Sql.getValue(itemLiberacao.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(itemLiberacao.nuPedido)).append(",");
        sql.append(Sql.getValue(itemLiberacao.cdUsuarioLiberacao)).append(",");
        sql.append(Sql.getValue(itemLiberacao.nmUsuario)).append(",");
        sql.append(Sql.getValue(itemLiberacao.nuCarimbo)).append(",");
        sql.append(Sql.getValue(itemLiberacao.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(itemLiberacao.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemLiberacao itemLiberacao = (ItemLiberacao) domain;
        sql.append(" NMUSUARIO = ").append(Sql.getValue(itemLiberacao.nmUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(itemLiberacao.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(itemLiberacao.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(itemLiberacao.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ItemLiberacao itemLiberacao = (ItemLiberacao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", itemLiberacao.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", itemLiberacao.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", itemLiberacao.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", itemLiberacao.nuPedido);
		sqlWhereClause.addAndCondition("CDUSUARIOLIBERACAO = ", itemLiberacao.cdUsuarioLiberacao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}