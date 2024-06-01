package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.MarcadorCliente;
import br.com.wmw.lavenderepda.business.domain.MarcadorPedido;
import br.com.wmw.lavenderepda.business.domain.MarcadorProduto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class MarcadorDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Marcador();
	}

    private static MarcadorDbxDao instance;

    public MarcadorDbxDao() {
        super(Marcador.TABLE_NAME); 
    }
    
    public static MarcadorDbxDao getInstance() {
        if (instance == null) {
            instance = new MarcadorDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Marcador marcador = new Marcador();
        marcador.rowKey = rs.getString("rowkey");
        marcador.cdMarcador = rs.getString("cdMarcador");
        marcador.dsMarcador = rs.getString("dsMarcador");
        marcador.nmEntidade = rs.getString("nmEntidade");
        marcador.dtInicioVigencia = rs.getDate("dtInicioVigencia");
        marcador.dtTerminovigencia = rs.getDate("dtTerminovigencia");
        marcador.imMarcadorAtivo = rs.getBytes("imMarcadorAtivo");
        marcador.nuSequencia = rs.getInt("nuSequencia");
        marcador.cdTipomarcador = rs.getInt("cdTipomarcador");
        marcador.nuCarimbo = rs.getInt("nuCarimbo");
        marcador.flTipoAlteracao = rs.getString("flTipoAlteracao");
        marcador.cdUsuario = rs.getString("cdUsuario");
        marcador.flFiltroSelecionado = rs.getString("flFiltroSelecionado");
        return marcador;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDMARCADOR,");
        sql.append(" tb.DSMARCADOR,");
        sql.append(" tb.NMENTIDADE,");
        sql.append(" tb.DTINICIOVIGENCIA,");
        sql.append(" tb.DTTERMINOVIGENCIA,");
        sql.append(" tb.IMMARCADORATIVO,");
        sql.append(" tb.NUSEQUENCIA,");
        sql.append(" tb.CDTIPOMARCADOR,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDUSUARIO," );
        sql.append(" tb.FLFILTROSELECIONADO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDMARCADOR,");
        sql.append(" DSMARCADOR,");
        sql.append(" NMENTIDADE,");
        sql.append(" DTINICIOVIGENCIA,");
        sql.append(" DTTERMINOVIGENCIA,");
        sql.append(" IMMARCADORATIVO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" CDTIPOMARCADOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO," );
		sql.append(" FLFILTROSELECIONADO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Marcador marcador = (Marcador) domain;
        sql.append(Sql.getValue(marcador.cdMarcador)).append(",");
        sql.append(Sql.getValue(marcador.dsMarcador)).append(",");
        sql.append(Sql.getValue(marcador.nmEntidade)).append(",");
        sql.append(Sql.getValue(marcador.dtInicioVigencia)).append(",");
        sql.append(Sql.getValue(marcador.dtTerminovigencia)).append(",");
        sql.append(Sql.getValue(marcador.imMarcadorAtivo)).append(",");
        sql.append(Sql.getValue(marcador.nuSequencia)).append(",");
        sql.append(Sql.getValue(marcador.cdTipomarcador)).append(",");
        sql.append(Sql.getValue(marcador.nuCarimbo)).append(",");
        sql.append(Sql.getValue(marcador.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(marcador.cdUsuario)).append(",");
		sql.append(Sql.getValue(marcador.flFiltroSelecionado));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Marcador marcador = (Marcador) domain;
        sql.append(" DSMARCADOR = ").append(Sql.getValue(marcador.dsMarcador)).append(",");
        sql.append(" NMENTIDADE = ").append(Sql.getValue(marcador.nmEntidade)).append(",");
        sql.append(" DTINICIOVIGENCIA = ").append(Sql.getValue(marcador.dtInicioVigencia)).append(",");
        sql.append(" DTTERMINOVIGENCIA = ").append(Sql.getValue(marcador.dtTerminovigencia)).append(",");
        sql.append(" IMMARCADORATIVO = ").append(Sql.getValue(marcador.imMarcadorAtivo)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(marcador.nuSequencia)).append(",");
        sql.append(" CDTIPOMARCADOR = ").append(Sql.getValue(marcador.cdTipomarcador)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(marcador.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(marcador.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(marcador.cdUsuario)).append(",");
		sql.append(" FLFILTROSELECIONADO = ").append(Sql.getValue(marcador.flFiltroSelecionado));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Marcador marcador = (Marcador) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDMARCADOR = ", marcador.cdMarcador);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
    public Vector buscaMarcadoresPorCliente(MarcadorCliente marcadorCliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT ");
        addSelectColumns(marcadorCliente, sql);
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" tb INNER JOIN TBLVPMARCADORCLIENTE ml on "); 
        sql.append("(ml.cdEmpresa = ").append(Sql.getValue(marcadorCliente.cdEmpresa));
		sql.append(" and ml.cdRepresentante = ").append(Sql.getValue(marcadorCliente.cdRepresentante));
		sql.append(" and ml.cdCliente = ").append(Sql.getValue(marcadorCliente.cdCliente));
		sql.append(" and ml.cdMarcador = tb.CDMARCADOR)");
		sql.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE())");
        sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR ");
        
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	Vector vector = new Vector(10);
        	while (rs.next()) {
        		vector.addElement(populate(marcadorCliente, rs));
        	}
        	return vector;
    	}
	}
    
    public Vector buscaMarcadoresDePedido(MarcadorPedido marcadorPedido) throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append("SELECT ");
        addSelectColumns(marcadorPedido, sql);
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(" tb INNER JOIN TBLVPMARCADORPEDIDO ml on "); 
        sql.append("(ml.cdEmpresa = ").append(Sql.getValue(marcadorPedido.cdEmpresa));
		sql.append(" and ml.cdRepresentante = ").append(Sql.getValue(marcadorPedido.cdRepresentante));
		sql.append(" and ml.flOrigemPedido = ").append(Sql.getValue(marcadorPedido.flOrigemPedido));
		sql.append(" and ml.nuPedido = ").append(Sql.getValue(marcadorPedido.nuPedido));
		sql.append(" and ml.cdMarcador = tb.CDMARCADOR)");
		sql.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE())");
        sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR ");
        
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
        	Vector vector = new Vector(10);
        	while (rs.next()) {
        		vector.addElement(populate(marcadorPedido, rs));
        	}
        	return vector;
    	}
	}

    public Vector buscaMarcadoresVigentes(String entidadeMarcador) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(domainFilter, sql);
    	sql.append(" FROM ");
    	sql.append(tableName);
    	sql.append(" tb WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE())");
    	sql.append(" AND tb.NMENTIDADE = ").append(Sql.getValue(entidadeMarcador));
    	sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector vector = new Vector();
    		while (rs.next()) {
    			vector.addElement(populate(domainFilter, rs));
    		}
    		return vector;
    	}
    }
    
    public Vector buscaMarcadoresVigentesDePedidos(String cdRepresentante) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(domainFilter, sql);
    	sql.append(" FROM ");
    	sql.append(tableName);
    	sql.append(" tb WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE())");
    	sql.append(" AND EXISTS (SELECT DISTINCT CDMARCADOR FROM TBLVPMARCADORPEDIDO WHERE CDMARCADOR = tb.CDMARCADOR ");
    	if (ValueUtil.isNotEmpty(cdRepresentante)) {
    		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
    	}
    	sql.append(")");
    	sql.append(" AND tb.NMENTIDADE = 'PEDIDO'");
    	sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector vector = new Vector();
    		while (rs.next()) {
    			vector.addElement(populate(domainFilter, rs));
    		}
    		return vector;
    	}
    }

	public HashMap<String, Marcador> buscaMarcadoresVigentesHash(String nmEntidade) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT ");
    	addSelectColumns(null, sql);
    	sql.append(" FROM ");
    	sql.append(tableName);
		sql.append(" tb WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE())");
		sql.append(" AND tb.NMENTIDADE = '").append(nmEntidade).append("'");
		sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			HashMap<String, Marcador> marcadoresHash = new HashMap<>();
			Marcador marcador;
			while (rs.next()) {
				marcador = new Marcador();
				marcador.cdMarcador = rs.getString("cdMarcador");
				marcador.imMarcadorAtivo = rs.getBytes("imMarcadorAtivo");
				marcadoresHash.put(marcador.cdMarcador, marcador);
			}
			return marcadoresHash;
		}
	}

    public Vector findMarcadoresByProduto(MarcadorProduto filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT DSMARCADOR, IMMARCADORATIVO FROM ").append(tableName).append(" TB")
    	.append(" JOIN TBLVPMARCADORPRODUTO MPRODUTO ON")
    	.append(" MPRODUTO.CDMARCADOR = TB.CDMARCADOR")
    	.append(" WHERE (COALESCE(DTINICIOVIGENCIA, DATE()) <= DATE() AND COALESCE(DTTERMINOVIGENCIA, DATE()) >= DATE()) AND TB.NMENTIDADE = ").append(Sql.getValue("PRODUTO")).append(" AND")
    	.append(" MPRODUTO.CDEMPRESA = ").append(Sql.getValue(filter.cdEmpresa)).append(" AND")
    	.append(" MPRODUTO.CDREPRESENTANTE = ").append(Sql.getValue(filter.cdRepresentante)).append(" AND")
    	.append(" MPRODUTO.CDPRODUTO = ").append(Sql.getValue(filter.cdProduto));
    	sql.append(" group by MPRODUTO.CDEMPRESA, MPRODUTO.CDREPRESENTANTE, MPRODUTO.CDPRODUTO, TB.CDMARCADOR ");
	    sql.append(" ORDER BY tb.NUSEQUENCIA, tb.DSMARCADOR");
	    try (Statement st = getCurrentDriver().getStatement();
	    		ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector vector = new Vector();
    		Marcador marcador;
    		while (rs.next()) {
    			marcador = new Marcador();
    			marcador.dsMarcador = rs.getString("dsMarcador");
    			marcador.imMarcadorAtivo = rs.getBytes("imMarcadorAtivo");
    			vector.addElement(marcador);
    		}
    		return vector;
    	}
    }
    
}