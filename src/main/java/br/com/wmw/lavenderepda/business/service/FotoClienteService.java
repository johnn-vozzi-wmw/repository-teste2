package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.FotoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoClienteDao;
import totalcross.io.File;
import totalcross.util.Date;
import totalcross.util.Vector;

public class FotoClienteService extends CrudService {

    private static FotoClienteService instance;
    
    public static FotoClienteService getInstance() {
        if (instance == null) {
            instance = new FotoClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FotoClienteDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public int getSequencialCdFotoCliente(Cliente cliente) throws SQLException {
    	FotoCliente fotoCliente = new FotoCliente();
    	fotoCliente.cdEmpresa = cliente.cdEmpresa;
    	fotoCliente.cdRepresentante = cliente.cdRepresentante;
		fotoCliente.cdCliente = cliente.cdCliente;
		return countByExample(fotoCliente);
	}
    
    private int getTamanhoFoto(String nmFoto) {
    	try {
    		File file = FileUtil.openFile(Cliente.getPathImg() + "/" + nmFoto, File.READ_ONLY);
    		return file.getSize();
		} catch (Throwable e) {
			return 0;
		}
    }
    
    public void insereFotoCliente(Cliente cliente, String nmFoto, int cdFotoCliente) throws SQLException {
    	FotoCliente fotoCliente = new FotoCliente();
    	fotoCliente.cdEmpresa = cliente.cdEmpresa;
    	fotoCliente.cdRepresentante = cliente.cdRepresentante;
		fotoCliente.cdCliente = cliente.cdCliente;
		fotoCliente.nmFoto = nmFoto;
		fotoCliente.cdFotoCliente = cdFotoCliente;
		fotoCliente.nuTamanho = getTamanhoFoto(nmFoto);
		fotoCliente.dtModificacao = DateUtil.getCurrentDate();
		insert(fotoCliente);
	}
    
    public void excluiFotoCliente(Cliente cliente, String nmFoto) throws SQLException {
    	FotoCliente fotoCliente = new FotoCliente();
    	fotoCliente.cdEmpresa = cliente.cdEmpresa;
    	fotoCliente.cdRepresentante = cliente.cdRepresentante;
		fotoCliente.cdCliente = cliente.cdCliente;
    	fotoCliente.nmFoto = nmFoto;
    	delete(fotoCliente);
    	FileUtil.deleteFile(Cliente.getPathImg() + "/" + nmFoto);
	}
	
	public boolean isPermiteExcluirFoto(Cliente cliente, String nmFoto) throws SQLException {
		FotoCliente fotoClienteFilter = new FotoCliente();
    	fotoClienteFilter.cdEmpresa = cliente.cdEmpresa;
    	fotoClienteFilter.cdRepresentante = cliente.cdRepresentante;
    	fotoClienteFilter.cdCliente = cliente.cdCliente;
    	fotoClienteFilter.nmFoto = nmFoto;
    	FotoCliente fotoCliente = (FotoCliente) findByRowKey(fotoClienteFilter.getRowKey());
    	return BaseDomain.FLTIPOALTERACAO_ALTERADO.equals(fotoCliente.flTipoAlteracao);
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoClienteList = FotoClienteDao.getInstance().findAllImagesToSend();
		for (int i = 0; i < fotoClienteList.size(); i++) {
			FotoCliente fotoCliente = (FotoCliente) fotoClienteList.items[i];
			imageList.addElement(fotoCliente.nmFoto);
		}
		return imageList;
	}
	
	public void deleteFotoClienteByFotoClienteErp() throws SQLException {
		if (LavenderePdaConfig.usaFotoCliente()) {
			Vector fotoClienteList = findAll();
			int size = fotoClienteList.size();
			for (int i = 0; i < size; i++) {
				FotoCliente fotoCliente = (FotoCliente) fotoClienteList.items[i];
				if (FotoClienteErpService.getInstance().isFotoRelacionadaComFotoClienteErp(fotoCliente)) {
					delete(fotoCliente);
					FileUtil.deleteFile(Cliente.getPathImg() + "/" + fotoCliente.nmFoto);
				}
			}
		}
	}
	
	public void deleteFotoClienteLimitePrazo() throws SQLException {
		FotoCliente fotoClienteFilter = new FotoCliente();
		fotoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		fotoClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Date dataLimite = DateUtil.getCurrentDate();
		DateUtil.decDay(dataLimite, LavenderePdaConfig.nuDiasPermanenciaFotoCliente);
		fotoClienteFilter.dtModificacaoFilter = dataLimite;
		deleteAllByExample(fotoClienteFilter);
	}

	public FotoCliente getFotoClienteBaseadoNaFotoClienteErp(FotoClienteErp fotoClienteErp) {
		FotoCliente fotoCliente = new FotoCliente();
		fotoCliente.cdEmpresa = fotoClienteErp.cdEmpresa;
		fotoCliente.cdRepresentante = fotoClienteErp.cdRepresentante;
		fotoCliente.cdCliente = fotoClienteErp.cdCliente;
		fotoCliente.nmFoto = fotoClienteErp.nmFoto;
		fotoCliente.nmFotoRelacionada = fotoClienteErp.nmFotoRelacionada;
		fotoCliente.cdFotoCliente = fotoClienteErp.cdFotoCliente;
		fotoCliente.nuTamanho = fotoClienteErp.nuTamanho;
		fotoCliente.dtModificacao = fotoClienteErp.dtModificacao;
		return fotoCliente;
	}

	public void deleteAllFotosExcluidas() {
		if (LavenderePdaConfig.usaFotoCliente() && LavenderePdaConfig.isPermiteExcluirFotosDeCliente()) {
			FotoClienteDao.getInstance().deleteAllFotosExcluidas();
		}
	}
	
}