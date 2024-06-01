package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FotoCliente;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class FotoClienteDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoCliente();
	}

    private static FotoClienteDao instance;
	public static final String TABLE_NAME = "TBLVPFOTOCLIENTE";

    public FotoClienteDao() {
        super(TABLE_NAME);
    }
    
    public static FotoClienteDao getInstance() {
        if (instance == null) {
            instance = new FotoClienteDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        FotoCliente fotoCliente = new FotoCliente();
        fotoCliente.rowKey = rs.getString("rowkey");
        fotoCliente.cdEmpresa = rs.getString("cdEmpresa");
        fotoCliente.cdRepresentante = rs.getString("cdRepresentante");
        fotoCliente.cdCliente = rs.getString("cdCliente");
        fotoCliente.nmFoto = rs.getString("nmFoto");
        fotoCliente.nmFotoRelacionada = rs.getString("nmFotoRelacionada");
        fotoCliente.cdFotoCliente = rs.getInt("cdFotoCliente");
        fotoCliente.nuTamanho = rs.getInt("nuTamanho");
        fotoCliente.dtModificacao = rs.getDate("dtModificacao");
        fotoCliente.flFotoExcluida = rs.getString("flFotoExcluida");
        fotoCliente.flTipoAlteracao =  rs.getString("flTipoAlteracao");
        fotoCliente.nuCarimbo = rs.getInt("nuCarimbo");
        fotoCliente.cdUsuario = rs.getString("cdUsuario");
        return fotoCliente;
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
        sql.append(" NMFOTO,");
        sql.append(" NMFOTORELACIONADA,");
        sql.append(" CDFOTOCLIENTE,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLFOTOEXCLUIDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    	sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" NMFOTO,");
        sql.append(" NMFOTORELACIONADA,");
        sql.append(" CDFOTOCLIENTE,");
        sql.append(" NUTAMANHO,");
        sql.append(" DTMODIFICACAO,");
        sql.append(" FLFOTOEXCLUIDA,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoCliente fotoCliente = (FotoCliente) domain;
        sql.append(Sql.getValue(fotoCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fotoCliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fotoCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(fotoCliente.nmFoto)).append(",");
        sql.append(Sql.getValue(fotoCliente.nmFotoRelacionada)).append(",");
        sql.append(Sql.getValue(fotoCliente.cdFotoCliente)).append(",");
        sql.append(Sql.getValue(fotoCliente.nuTamanho)).append(",");
        sql.append(Sql.getValue(fotoCliente.dtModificacao)).append(",");
        sql.append(Sql.getValue(fotoCliente.flFotoExcluida)).append(",");
        sql.append(Sql.getValue(fotoCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(fotoCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoCliente fotoCliente = (FotoCliente) domain;
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoCliente.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FotoCliente fotoCliente = (FotoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", fotoCliente.cdCliente);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoCliente.nmFoto);
		sqlWhereClause.addAndCondition("DTMODIFICACAO <= ", fotoCliente.dtModificacaoFilter);
		sql.append(sqlWhereClause.getSql());
    }
    
    public Vector findAllImagesToSend() throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where flTipoAlteracao != ''");
        sql.append(" and  flFotoExcluida != 'S'");
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector fotoClienteList = new Vector();
			while (rs.next()) {
				fotoClienteList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return fotoClienteList;
		}
	}
      
    public void deleteAllFotosExcluidas() {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" delete from ");
        sql.append(tableName);
        sql.append(" where flTipoAlteracao = ''");
        sql.append(" and  flFotoExcluida = 'S'");
        try {
        	executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
    }
    
}