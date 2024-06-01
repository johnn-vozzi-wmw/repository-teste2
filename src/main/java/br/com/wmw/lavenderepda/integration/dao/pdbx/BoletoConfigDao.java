package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class BoletoConfigDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new BoletoConfig();
	}

    private static BoletoConfigDao instance;

    public BoletoConfigDao() {
        super(BoletoConfig.TABLE_NAME);
    }
    
    public static BoletoConfigDao getInstance() {
        if (instance == null) {
            instance = new BoletoConfigDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        BoletoConfig boletoConfig = new BoletoConfig();
        boletoConfig.rowKey = rs.getString("rowkey");
        boletoConfig.cdBoletoConfig = rs.getString("cdBoletoConfig");
        boletoConfig.nmBanco = rs.getString("nmBanco");
    	boletoConfig.imBanco = rs.getBytes("imBanco");
        boletoConfig.cdBeneficiario = rs.getString("cdBeneficiario");
        boletoConfig.nuCarimbo = rs.getInt("nuCarimbo");
        boletoConfig.flTipoAlteracao = rs.getString("flTipoAlteracao");
        boletoConfig.nuAgenciaCodigoCedente = rs.getString("nuAgenciaCodigoCedente");
        boletoConfig.nuBanco = rs.getString("nuBanco");
        boletoConfig.nuCarteira = rs.getString("nuCarteira");
        boletoConfig.cdUsuario = rs.getString("cdUsuario");
        boletoConfig.flModoBoleto = rs.getString("flModoBoleto");
        boletoConfig.nuAgencia = rs.getString("nuAgencia");
        boletoConfig.nuConta = rs.getString("nuConta");
        boletoConfig.dsLocalPagamento = rs.getString("dsLocalPagamento");
        boletoConfig.nuByteBoleto = rs.getInt("nuByteBoleto");
        boletoConfig.dsCarteira = rs.getString("dsCarteira");
        boletoConfig.dsEspecieBanco = rs.getString("dsEspecieBanco");
        boletoConfig.dsMensagemSacador = rs.getString("dsMensagemSacador");
        return boletoConfig;
    }
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDBOLETOCONFIG,");
        sql.append(" NMBANCO,");
        sql.append(" CDBENEFICIARIO,");
        sql.append(" IMBANCO,");
        sql.append(" NUCARIMBO,");
        sql.append(" NUAGENCIACODIGOCEDENTE,");
        sql.append(" NUBANCO,");
        sql.append(" NUCARTEIRA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLMODOBOLETO,");
        sql.append(" NUAGENCIA,");
        sql.append(" NUCONTA,");
        sql.append(" DSLOCALPAGAMENTO,");
        sql.append(" NUBYTEBOLETO,");
        sql.append(" DSCARTEIRA,");
        sql.append(" DSESPECIEBANCO,");
        sql.append(" DSMENSAGEMSACADOR");
    }
    
    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	public Vector findAllDistinct() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT NUBANCO, MIN(NMBANCO) NMBANCO FROM TBLVPBOLETOCONFIG GROUP BY NUBANCO ");
		try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector boletoConfigList = new Vector();
			BoletoConfig boletoConfig = null;
			while (rs.next()) {
				boletoConfig = new BoletoConfig();
				boletoConfig.nuBanco = rs.getString("nuBanco");
				boletoConfig.nmBanco = rs.getString("nmBanco");
				boletoConfigList.addElement(boletoConfig);
			}
			return boletoConfigList;
		}
	}
    
}