package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FotoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoClienteErpDao;
import totalcross.util.Vector;

public class FotoClienteErpService extends CrudService {

    private static FotoClienteErpService instance;
    
    private FotoClienteErpService() {
        //--
    }
    
    public static FotoClienteErpService getInstance() {
        if (instance == null) {
            instance = new FotoClienteErpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FotoClienteErpDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public Vector findAllNaoAlterados() throws java.sql.SQLException {
    	return ((FotoClienteErpDao)getCrudDao()).findAllNaoAlterados();
    }
    
	public boolean isExcluiFotoClienteErp(Cliente cliente, String nmFoto) throws SQLException {
		FotoClienteErp fotoClienteErpFilter = new FotoClienteErp();
		fotoClienteErpFilter.cdEmpresa = cliente.cdEmpresa;
		fotoClienteErpFilter.cdRepresentante = cliente.cdRepresentante;
		fotoClienteErpFilter.cdCliente = cliente.cdCliente;
		fotoClienteErpFilter.nmFoto = nmFoto;
		return findByRowKey(fotoClienteErpFilter.getRowKey()) != null;
	}
	
	public void excluiFotoClienteErp(Cliente cliente, String nmFoto) throws SQLException {
		FotoClienteErp fotoClienteErpFilter = new FotoClienteErp();
		fotoClienteErpFilter.cdEmpresa = cliente.cdEmpresa;
		fotoClienteErpFilter.cdRepresentante = cliente.cdRepresentante;
		fotoClienteErpFilter.cdCliente = cliente.cdCliente;
		fotoClienteErpFilter.nmFoto = nmFoto;
		FotoClienteErp fotoClienteErp = (FotoClienteErp) findByRowKey(fotoClienteErpFilter.getRowKey());
		FotoCliente fotoCliente = FotoClienteService.getInstance().getFotoClienteBaseadoNaFotoClienteErp(fotoClienteErp);
		fotoCliente.flFotoExcluida = ValueUtil.VALOR_SIM;
		try {
			FotoClienteService.getInstance().insert(fotoCliente);
		} catch (Throwable ex) {
		}
		delete(fotoClienteErp);
		FileUtil.deleteFile(Cliente.getPathImg() + "/" + nmFoto);
	}

	public boolean isFotoRelacionadaComFotoClienteErp(FotoCliente fotoCliente) throws SQLException {
		FotoClienteErp fotoClienteErpFilter = new FotoClienteErp();
		fotoClienteErpFilter.cdEmpresa = fotoCliente.cdEmpresa;
		fotoClienteErpFilter.cdRepresentante = fotoCliente.cdRepresentante;
		fotoClienteErpFilter.cdCliente = fotoCliente.cdCliente;
		fotoClienteErpFilter.nmFotoRelacionada = fotoCliente.nmFoto;
		return countByExample(fotoClienteErpFilter) > 0;
	}
	

	public boolean possuiFotoGeradaCargaInicial() throws SQLException {
		FotoClienteErp fotoClienteErpFilter = new FotoClienteErp();
		fotoClienteErpFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		return countByExample(fotoClienteErpFilter) > 0;
	}
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		((FotoClienteErpDao)getCrudDao()).updateResetReceberFotosNovamente();
	}

	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		((FotoClienteErpDao)getCrudDao()).updateAllFlTipoAlteracaoInserido();
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		((FotoClienteErpDao)getCrudDao()).updateReceberFotosAoFalharRecebimentoCargaInicial();
	}

	public int countFotoCargaInicial() throws SQLException {
		return ((FotoClienteErpDao)getCrudDao()).countFotoCargaInicial();
	}
	
}