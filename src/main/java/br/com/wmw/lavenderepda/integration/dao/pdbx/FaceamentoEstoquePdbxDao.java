package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FaceamentoEstoque;
import totalcross.sql.ResultSet;

public class FaceamentoEstoquePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FaceamentoEstoque();
	}

    private static FaceamentoEstoquePdbxDao instance;

    public FaceamentoEstoquePdbxDao() {
        super(FaceamentoEstoque.TABLE_NAME);
    }

    public static FaceamentoEstoquePdbxDao getInstance() {
        if (instance == null) {
            instance = new FaceamentoEstoquePdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FaceamentoEstoque faceamentoEstoque = new FaceamentoEstoque();
        faceamentoEstoque.rowKey = rs.getString("rowkey");
        faceamentoEstoque.cdEmpresa = rs.getString("cdEmpresa");
        faceamentoEstoque.cdRepresentante = rs.getString("cdRepresentante");
        faceamentoEstoque.cdCliente = rs.getString("cdCliente");
        faceamentoEstoque.cdProduto = rs.getString("cdProduto");
        faceamentoEstoque.dtCadastro = rs.getDate("dtCadastro");
        faceamentoEstoque.qtEstoqueAtual = ValueUtil.round(rs.getDouble("qtEstoqueatual"));
        faceamentoEstoque.flUltilizadoPedidoDtAtual = rs.getString("flUltilizadoPedidoDtAtual");
        faceamentoEstoque.hrCadastro = rs.getString("hrCadastro");
        faceamentoEstoque.vlFatorFaceamento = rs.getDouble("vlFatorFaceamento");
        faceamentoEstoque.qtSugestaoVenda = rs.getDouble("qtSugestaoVenda");
        faceamentoEstoque.qtSugestaoVendaRep = rs.getDouble("qtSugestaoVendaRep");
        faceamentoEstoque.cdUsuario = rs.getString("cdUsuario");
        faceamentoEstoque.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return faceamentoEstoque;
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
        sql.append(" CDPRODUTO,");
        sql.append(" DTCADASTRO,");
        sql.append(" QTESTOQUEATUAL,");
        sql.append(" FLULTILIZADOPEDIDODTATUAL,");
        sql.append(" HRCADASTRO,");
        sql.append(" VLFATORFACEAMENTO,");
        sql.append(" QTSUGESTAOVENDA,");
        sql.append(" QTSUGESTAOVENDAREP,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTCADASTRO,");
        sql.append(" QTESTOQUEATUAL,");
        sql.append(" FLULTILIZADOPEDIDODTATUAL,");
        sql.append(" HRCADASTRO,");
        sql.append(" VLFATORFACEAMENTO,");
        sql.append(" QTSUGESTAOVENDA,");
        sql.append(" QTSUGESTAOVENDAREP,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FaceamentoEstoque faceamentoEstoque = (FaceamentoEstoque) domain;
        sql.append(Sql.getValue(faceamentoEstoque.cdEmpresa)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.cdRepresentante)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.cdCliente)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.cdProduto)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.dtCadastro)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.qtEstoqueAtual)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.flUltilizadoPedidoDtAtual)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.hrCadastro)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.vlFatorFaceamento)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.qtSugestaoVenda)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.qtSugestaoVendaRep)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.cdUsuario)).append(",");
        sql.append(Sql.getValue(faceamentoEstoque.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FaceamentoEstoque faceamentoEstoque = (FaceamentoEstoque) domain;
        sql.append(" QTESTOQUEATUAL = ").append(Sql.getValue(faceamentoEstoque.qtEstoqueAtual)).append(",");
        sql.append(" FLULTILIZADOPEDIDODTATUAL = ").append(Sql.getValue(faceamentoEstoque.flUltilizadoPedidoDtAtual)).append(",");
        sql.append(" VLFATORFACEAMENTO = ").append(Sql.getValue(faceamentoEstoque.vlFatorFaceamento)).append(",");
        sql.append(" QTSUGESTAOVENDA = ").append(Sql.getValue(faceamentoEstoque.qtSugestaoVenda)).append(",");
        sql.append(" QTSUGESTAOVENDAREP = ").append(Sql.getValue(faceamentoEstoque.qtSugestaoVendaRep)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(faceamentoEstoque.flTipoAlteracao));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FaceamentoEstoque faceamentoEstoque = (FaceamentoEstoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", faceamentoEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", faceamentoEstoque.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", faceamentoEstoque.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", faceamentoEstoque.cdProduto);
		sqlWhereClause.addAndCondition("DTCADASTRO = ", faceamentoEstoque.dtCadastro);
		sqlWhereClause.addAndCondition("FLULTILIZADOPEDIDODTATUAL = ", faceamentoEstoque.flUltilizadoPedidoDtAtual);
		sql.append(sqlWhereClause.getSql());
    }
}