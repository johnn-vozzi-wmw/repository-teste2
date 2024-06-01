package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class DocumentoAnexoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DocumentoAnexo();
	}

    private static DocumentoAnexoDbxDao instance = null;
	

    public DocumentoAnexoDbxDao() {
        super(DocumentoAnexo.TABLE_NAME);
    }
    
    public static DocumentoAnexoDbxDao getInstance() {
        if (instance == null) {
            instance = new DocumentoAnexoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DocumentoAnexo documentoAnexo = new DocumentoAnexo();
        documentoAnexo.rowKey = rs.getString("rowkey");
        documentoAnexo.cdEmpresa = rs.getString("cdEmpresa");
        documentoAnexo.cdRepresentante = rs.getString("cdRepresentante");
        documentoAnexo.nmEntidade = rs.getString("nmEntidade");
        documentoAnexo.dsChave = rs.getString("dsChave");
        documentoAnexo.cdDocumentoAnexo = rs.getInt("cdDocumentoAnexo");
        documentoAnexo.nmArquivo = rs.getString("nmArquivo");
        documentoAnexo.baArquivo = rs.getString("baArquivo");
        documentoAnexo.dtDocumento = rs.getDate("dtDocumento");
        documentoAnexo.cdUsuario = rs.getString("cdUsuario");
        documentoAnexo.nuCarimbo = rs.getInt("nuCarimbo");
        documentoAnexo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return documentoAnexo;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NMENTIDADE,");
        sql.append(" DSCHAVE,");
        sql.append(" CDDOCUMENTOANEXO,");
        sql.append(" NMARQUIVO,");
        sql.append(" BAARQUIVO,");
        sql.append(" DTDOCUMENTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NMENTIDADE,");
        sql.append(" DSCHAVE,");
        sql.append(" CDDOCUMENTOANEXO,");
        sql.append(" NMARQUIVO,");
        sql.append(" BAARQUIVO,");
        sql.append(" DTDOCUMENTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        DocumentoAnexo documentoAnexo = (DocumentoAnexo) domain;
        sql.append(Sql.getValue(documentoAnexo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(documentoAnexo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(documentoAnexo.nmEntidade)).append(",");
        sql.append(Sql.getValue(documentoAnexo.dsChave)).append(",");
        sql.append(Sql.getValue(documentoAnexo.cdDocumentoAnexo)).append(",");
        sql.append(Sql.getValue(documentoAnexo.nmArquivo)).append(",");
        sql.append(Sql.getValue(documentoAnexo.baArquivo)).append(",");
        sql.append(Sql.getValue(documentoAnexo.dtDocumento)).append(",");
        sql.append(Sql.getValue(documentoAnexo.cdUsuario)).append(",");
        sql.append(Sql.getValue(documentoAnexo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(documentoAnexo.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        DocumentoAnexo documentoAnexo = (DocumentoAnexo) domain;
        sql.append(" NMARQUIVO = ").append(Sql.getValue(documentoAnexo.nmArquivo)).append(",");
        sql.append(" BAARQUIVO = ").append(Sql.getValue(documentoAnexo.baArquivo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(documentoAnexo.cdUsuario)).append(",");
        sql.append(" DTDOCUMENTO = ").append(Sql.getValue(documentoAnexo.dtDocumento)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(documentoAnexo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(documentoAnexo.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        DocumentoAnexo documentoAnexo = (DocumentoAnexo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", documentoAnexo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", documentoAnexo.cdRepresentante);
		sqlWhereClause.addAndCondition("NMENTIDADE = ", documentoAnexo.nmEntidade);
		sqlWhereClause.addAndCondition("DSCHAVE = ", documentoAnexo.dsChave);
		sqlWhereClause.addAndCondition("DTDOCUMENTO < ", documentoAnexo.dtDocumento);
		sqlWhereClause.addAndCondition("CDDOCUMENTOANEXO = ", documentoAnexo.cdDocumentoAnexo);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
    public Vector findAllDocAnexoToSend() throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where flTipoAlteracao != ''");
        try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector docAnexoList = new Vector();
			while (rs.next()) {
				docAnexoList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return docAnexoList;
		}
	}
}