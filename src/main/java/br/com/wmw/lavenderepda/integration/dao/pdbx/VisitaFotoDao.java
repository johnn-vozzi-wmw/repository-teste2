package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;


public class VisitaFotoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VisitaFoto();
	}

    private static VisitaFotoDao instance;
	public static final String TABLE_NAME = "TBLVPVISITAFOTO";

    public VisitaFotoDao() {
        super(TABLE_NAME);
    }

    public static VisitaFotoDao getInstance() {
        if (instance == null) {
            instance = new VisitaFotoDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        VisitaFoto visitaFoto = new VisitaFoto();
        visitaFoto.rowKey = rs.getString("rowkey");
        visitaFoto.cdEmpresa = rs.getString("cdEmpresa");
        visitaFoto.cdRepresentante = rs.getString("cdRepresentante");
        visitaFoto.flOrigemVisita = rs.getString("flOrigemVisita");
        visitaFoto.cdVisita = rs.getString("cdVisita");
        visitaFoto.cdFoto = rs.getInt("cdFoto");
        visitaFoto.imFoto = rs.getString("imFoto");
        visitaFoto.flEnviadoServidor = rs.getString("flEnviadoServidor");
        visitaFoto.nuCarimbo = rs.getInt("nuCarimbo");
        visitaFoto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        visitaFoto.cdUsuario = rs.getString("cdUsuario");
        return visitaFoto;
    }


    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDVISITA,");
        sql.append(" CDFOTO,");
        sql.append(" IMFOTO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMVISITA,");
        sql.append(" CDVISITA,");
        sql.append(" CDFOTO,");
        sql.append(" IMFOTO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaFoto visitaFoto = (VisitaFoto) domain;
        sql.append(Sql.getValue(visitaFoto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(visitaFoto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(visitaFoto.flOrigemVisita)).append(",");
        sql.append(Sql.getValue(visitaFoto.cdVisita)).append(",");
        sql.append(Sql.getValue(visitaFoto.cdFoto)).append(",");
        sql.append(Sql.getValue(visitaFoto.imFoto)).append(",");
        sql.append(Sql.getValue(visitaFoto.flEnviadoServidor)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(visitaFoto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(visitaFoto.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VisitaFoto visitaFoto = (VisitaFoto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", visitaFoto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", visitaFoto.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMVISITA = ", visitaFoto.flOrigemVisita);
		sqlWhereClause.addAndCondition("CDVISITA = ", visitaFoto.cdVisita);
		sqlWhereClause.addAndCondition("CDFOTO = ", visitaFoto.cdFoto);
		sqlWhereClause.addAndCondition("IMFOTO = ", visitaFoto.imFoto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		VisitaFoto visitaFoto = (VisitaFoto) domain;
		sql.append(" FLENVIADOSERVIDOR= ").append(Sql.getValue(visitaFoto.flEnviadoServidor));
	}

	public Vector findAllImagesToSend() throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where flEnviadoServidor != ").append(Sql.getValue(ValueUtil.VALOR_SIM));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector visitaFotoList = new Vector();
			while (rs.next()) {
				visitaFotoList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return visitaFotoList;
		}
	}
	
	public void updateFotosVisitaParaEnvio(String cdEmpresa, String cdRepresentante, String flOrigemVisita, String cdVisita, String flTipoAlteracao) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(flTipoAlteracao));
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" and FLORIGEMVISITA = ").append(Sql.getValue(flOrigemVisita));
		sql.append(" and CDVISITA = ").append(Sql.getValue(cdVisita));
		sql.append(" and FLENVIADOSERVIDOR != ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		//--
		try {
			executeUpdate(sql.toString());			
		} catch (ApplicationException e) {
			//Significa que apenas não houve alteração de registros, pois a visita não possui fotos
		}
	}

}