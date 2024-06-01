package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Place;
import br.com.wmw.lavenderepda.business.domain.dto.ListaLeadsDTO;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlaceDao;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONArray;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;
import totalcross.util.Vector;

import java.sql.SQLException;

public class PlaceService extends CrudService {

    private static PlaceService instance;
    
    private PlaceService() { }
    
    public static PlaceService getInstance() {
        if (instance == null) {
            instance = new PlaceService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PlaceDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	public Vector buscarLista(ListaLeadsDTO listaDTO) throws Exception {
		return montaPlaces(SyncManager.geraListaLeads(listaDTO));
	}
	
	private Vector montaPlaces(String listPlaces) {
		Vector placeList = new Vector();
		try {
			if (ValueUtil.isNotEmpty(listPlaces)) {
				JSONArray jsonPlacesArray = new JSONArray(listPlaces);
				if (jsonPlacesArray != null) {
					for (int i = 0; i < jsonPlacesArray.length(); i++) {
						JSONObject jsonPlace = (JSONObject) jsonPlacesArray.get(i);
						Place places = new Place();
						places.cdEmpresa = SessionLavenderePda.cdEmpresa;
						places.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
						places.cdPlaceId = jsonPlace.getString("placeID");
						places.nmPlace = jsonPlace.getString("nome").replace("'", "");
						places.dsEndereco = jsonPlace.getString("endereco");
						places.dsTelefone = jsonPlace.get("telefone") + "";
						if (places.dsTelefone.equals("null")) {
							places.dsTelefone = "";
						}
						places.dsUrl = jsonPlace.get("url") + "";
						if (places.dsUrl.equals("null")) {
							places.dsUrl = "";
						}
						places.cdLatitude = jsonPlace.getDouble("cdLatitude");
						places.cdLongitude = jsonPlace.getDouble("cdLongitude");
						if (LavenderePdaConfig.isUsaCategoriaLeads()) {
							places.cdCategoria = jsonPlace.get("cdCategoria") + "";
							if (places.cdCategoria.equals("null")) {
								places.cdCategoria = "";
							}
						}
						places.flTipoAlteracao = "I";
						places.vlRating = Double.parseDouble(!ValueUtil.valueEquals(jsonPlace.get("rating"), null) ? jsonPlace.get("rating") + "" : "0");
						places.cdUsuario = Session.getCdUsuario();
						places.nuOrdem = i;
						places.flQualificado = Place.TIPO_EM_ANALISE;
						placeList.addElement(places);
					}
					return placeList;
				}
			}
		} catch (JSONException e) {
			throw new ValidationException(Messages.LISTALEADS_LISTA_GEOCODE_ENDERECO);
		} catch (Exception e) {
			throw new ValidationException(MessageUtil.getMessage(Messages.LISTALEADS_ERRO_LEADS, e.getMessage()));
		}
		return placeList;
	}
 
	public void insertOrUpdate(Place place) throws SQLException {
		if (PlaceDao.getInstance().countByRowKey(place) > 0) {
			update(place);
		} else {
			insert(place);
		}
	}

}