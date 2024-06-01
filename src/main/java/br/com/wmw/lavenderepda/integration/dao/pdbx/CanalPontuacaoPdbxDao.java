package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class CanalPontuacaoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PontExtPed();
	}

    private static CanalPontuacaoPdbxDao instance;

    public CanalPontuacaoPdbxDao() {
        super(PontExtPed.TABLE_NAME); // Os segmento são carregados da tabela de pontuação
    }

    public static CanalPontuacaoPdbxDao getInstance() { return instance == null ? instance = new CanalPontuacaoPdbxDao() : instance; }
    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {return null;}
	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {}
	@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {return null;}
	@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}

	public Vector findAllCanalInPontExtPed() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT DISTINCT CDCANAL FROM " + tableName);
        sql.append(" ORDER BY CDCANAL ASC ");
        try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector();
			while (rs.next()) {
				final String valor = rs.getString("cdCanal");
				if (ValueUtil.isEmpty(valor)) continue;
				result.addElement(valor);
			}
			return result;
		}
	}

}