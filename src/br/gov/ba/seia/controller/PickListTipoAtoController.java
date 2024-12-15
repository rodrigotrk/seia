package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.Player;



@Named("pickListTipoAtoController")
@RequestScoped
public class PickListTipoAtoController {
	
	 private DualListModel<Player> players;

		private DualListModel<String> cities;

		public PickListTipoAtoController() {
	
			List<String> citiesSource = new ArrayList<String>();
			List<String> citiesTarget = new ArrayList<String>();

			citiesSource.add("TCRA");
			citiesSource.add("LICENÇA");
			citiesSource.add("AUTORIZAÇÃO AMBIENTAL");
			citiesSource.add("DECLARATÓRIO");
			citiesSource.add("LICENÇA DE LOCALIZAÇÃO");
			citiesSource.add("LICENÇA DE IMPLANTAÇÃO");
			citiesSource.add("LICENÇA DE OPERAÇÃO");
			

			cities = new DualListModel<String>(citiesSource, citiesTarget);
		}

		public DualListModel<Player> getPlayers() {
			return players;
		}
		public void setPlayers(DualListModel<Player> players) {
			this.players = players;
		}

		public DualListModel<String> getCities() {
			return cities;
		}
		public void setCities(DualListModel<String> cities) {
			this.cities = cities;
		}
	
	

}
