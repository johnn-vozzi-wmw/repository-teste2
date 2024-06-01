package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.lavenderepda.business.domain.MotNaoVendaProduto;
import totalcross.sql.ResultSet;

public class MotNaoVendaProdutoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotNaoVendaProduto();
	}

    private static MotNaoVendaProdutoDao instance;
	

    public MotNaoVendaProdutoDao() {
        super(MotNaoVendaProduto.TABLE_NAME);
    }
    
    public static MotNaoVendaProdutoDao getInstance() {
        if (instance == null) {
            instance = new MotNaoVendaProdutoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        MotNaoVendaProduto motNaoVendaProduto = new MotNaoVendaProduto();
        motNaoVendaProduto.rowKey = rs.getString("rowkey");
        motNaoVendaProduto.cdMotivo = rs.getString("cdMotivo");
        motNaoVendaProduto.dsMotivo = rs.getString("dsMotivo");
        motNaoVendaProduto.flExigeJustificativa = rs.getString("flExigeJustificativa");
        motNaoVendaProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        motNaoVendaProduto.nuCarimbo = rs.getInt("nuCarimbo");
        motNaoVendaProduto.cdUsuario = rs.getString("cdUsuario");
        return motNaoVendaProduto;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDMOTIVO,");
        sql.append(" DSMOTIVO,");
        sql.append(" FLEXIGEJUSTIFICATIVA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    
}