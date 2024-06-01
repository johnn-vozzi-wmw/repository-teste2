package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.CargaProduto;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusCargaProdComboBox;
import totalcross.sql.ResultSet;
import totalcross.util.Date;

public class CargaProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CargaProduto();
	}

    private static CargaProdutoDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPCARGAPRODUTO";

    public CargaProdutoDbxDao(String newTableName) {
        super(newTableName);
    }
    
    public static CargaProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new CargaProdutoDbxDao(TABLE_NAME);
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        CargaProduto cargaProduto = new CargaProduto();
        cargaProduto.rowKey = rs.getString("rowkey");
        cargaProduto.cdEmpresa = rs.getString("cdEmpresa");
        cargaProduto.cdRepresentante = rs.getString("cdRepresentante");
        cargaProduto.cdProduto = rs.getString("cdProduto");
        cargaProduto.cdCargaProduto = rs.getString("cdCargaProduto");
        cargaProduto.qtSolicitado = ValueUtil.round(rs.getDouble("qtSolicitado"));
        cargaProduto.dtCadastro = rs.getDate("dtCadastro");
        cargaProduto.hrCadastro = rs.getString("hrCadastro");
        cargaProduto.cdUsuario = rs.getString("cdUsuario");
        cargaProduto.nuCarimbo = rs.getInt("nuCarimbo");
        cargaProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        cargaProduto.dtAlteracao = rs.getDate("dtAlteracao");
        cargaProduto.hrAlteracao = rs.getString("hrAlteracao");
        cargaProduto.getProduto().cdProduto = rs.getString("cdProduto");
        cargaProduto.getProduto().dsProduto = rs.getString("dsProduto");
        cargaProduto.getProduto().dsReferencia = rs.getString("dsReferencia");
        return cargaProduto;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
    	sql.append(" PROD.DSPRODUTO,");
    	sql.append(" PROD.DSREFERENCIA,");
        sql.append(" TB.rowkey,");
        sql.append(" TB.CDEMPRESA,");
        sql.append(" TB.CDREPRESENTANTE,");
        sql.append(" TB.CDPRODUTO,");
        sql.append(" TB.CDCARGAPRODUTO,");
        sql.append(" TB.QTSOLICITADO,");
        sql.append(" TB.DTCADASTRO,");
        sql.append(" TB.HRCADASTRO,");
        sql.append(" TB.CDUSUARIO,");
        sql.append(" TB.NUCARIMBO,");
        sql.append(" TB.FLTIPOALTERACAO,");
        sql.append(" TB.DTALTERACAO,");
        sql.append(" TB.HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDCARGAPRODUTO,");
        sql.append(" QTSOLICITADO,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        CargaProduto cargaProduto = (CargaProduto) domain;
        sql.append(Sql.getValue(cargaProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(cargaProduto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(cargaProduto.cdProduto)).append(",");
        sql.append(Sql.getValue(cargaProduto.cdCargaProduto)).append(",");
        sql.append(Sql.getValue(cargaProduto.qtSolicitado)).append(",");
        sql.append(Sql.getValue(cargaProduto.dtCadastro)).append(",");
        sql.append(Sql.getValue(cargaProduto.hrCadastro)).append(",");
        sql.append(Sql.getValue(cargaProduto.cdUsuario)).append(",");
        sql.append(Sql.getValue(cargaProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(cargaProduto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(cargaProduto.dtAlteracao)).append(",");
        sql.append(Sql.getValue(cargaProduto.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        CargaProduto cargaProduto = (CargaProduto) domain;
        sql.append(" QTSOLICITADO = ").append(Sql.getValue(cargaProduto.qtSolicitado)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(cargaProduto.dtCadastro)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(cargaProduto.hrCadastro)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(cargaProduto.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(cargaProduto.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(cargaProduto.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(cargaProduto.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(cargaProduto.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        CargaProduto cargaProduto = (CargaProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", cargaProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", cargaProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDPRODUTO = ", cargaProduto.cdProduto);
		sqlWhereClause.addAndCondition("TB.CDCARGAPRODUTO = ", cargaProduto.cdCargaProduto);
		if (ValueUtil.isNotEmpty(cargaProduto.cdStatusFilter)) {
			if (StatusCargaProdComboBox.FL_STATUS_ENVIADO.equals(cargaProduto.cdStatusFilter)) {
				sqlWhereClause.addAndConditionForced("TB.FLTIPOALTERACAO = ", CargaProduto.FLTIPOALTERACAO_ORIGINAL);
			} else {
				sqlWhereClause.addAndConditionForced("TB.FLTIPOALTERACAO != ", CargaProduto.FLTIPOALTERACAO_ORIGINAL);
			}
		}
		//--
		sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
   		adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("PROD.DSPRODUTO", cargaProduto.getProduto().dsProduto, false);
   		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("PROD.CDPRODUTO", cargaProduto.getProduto().cdProdutoLikeFilter, false);
   		adicionouInicioBloco |= sqlWhereClause.addOrCondition("PROD.CDPRODUTO = ", cargaProduto.getProduto().cdProduto);
   		adicionouInicioBloco |= sqlWhereClause.addOrCondition("PROD.NUCODIGOBARRAS = ", cargaProduto.getProduto().nuCodigoBarras);
   		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("PROD.DSMARCA", cargaProduto.getProduto().dsMarca, false);
   		adicionouInicioBloco |= sqlWhereClause.addOrCondition("PROD.DSREFERENCIA = ", cargaProduto.getProduto().dsReferencia);
   		adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("PROD.DSREFERENCIA", cargaProduto.getProduto().dsReferenciaLikeFilter, false);
   		if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
    }
 
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	super.addJoin(domainFilter, sql);
    	sql.append(" LEFT OUTER JOIN TBLVPPRODUTO PROD ON");
    	sql.append(" PROD.CDEMPRESA = TB.CDEMPRESA");
    	sql.append(" AND PROD.CDREPRESENTANTE = TB.CDREPRESENTANTE");
    	sql.append(" AND PROD.CDPRODUTO = TB.CDPRODUTO");
    }

	public void deleteAllByDtCadastro(Date dtLimite) {
		StringBuffer sql = getSqlBuffer();
        sql.append(" DELETE FROM ").append(tableName);
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DTCADASTRO <= ", dtLimite);
		sql.append(sqlWhereClause.getSql());
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
	}
}