package org.nkigen.maps.viewer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.awt.AwtGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.swing.controller.MapViewComponentListener;
import org.mapsforge.map.swing.controller.MouseEventListener;
import org.mapsforge.map.swing.util.JavaUtilPreferences;
import org.mapsforge.map.swing.view.MainFrame;
import org.mapsforge.map.swing.view.MapView; 
import org.mapsforge.map.swing.view.WindowCloseDialog;

import com.sun.istack.internal.Nullable;
 

public class EQRMapViewer {

	private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE;
	private static final boolean SHOW_DEBUG_LAYERS = false;
	private static final String FRAME_TITLE = "EQR_VIEWER";
	private EQRMapViewer viewer;
	private MapView mapView;
	private final BoundingBox boundingBox;
	private final Model model;
	private File mapfile;
	private MainFrame mainFrame;
	
	private EQRMapViewer(String filename){
		mapfile = getMapFile(filename);
		viewer = new EQRMapViewer(filename);
		mapView = createMapView();
		boundingBox = addLayers(mapView, mapfile);
		model = mapView.getModel();
		mainFrame = new MainFrame();
	}
	
	public EQRMapViewer getInstance(@Nullable String filename){
		if(viewer == null){			
			PreferencesFacade preferencesFacade = new JavaUtilPreferences(Preferences.userNodeForPackage(EQRMapViewer.class));
			model.init(preferencesFacade);
			mainFrame.add(mapView);
			mainFrame.setTitle(FRAME_TITLE);
			mainFrame.addWindowListener(new WindowCloseDialog(mainFrame, model, preferencesFacade));
			mainFrame.setVisible(true);
			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					byte zoomLevel = LatLongUtils.zoomForBounds(model.mapViewDimension.getDimension(), boundingBox,
							model.displayModel.getTileSize());
					model.mapViewPosition.setMapPosition(new MapPosition(boundingBox.getCenterPoint(), zoomLevel));
				}
			});
		}
		return viewer;
	}

	private static BoundingBox addLayers(MapView mapView, File mapFile) {
		Layers layers = mapView.getLayerManager().getLayers();
		TileCache tileCache = createTileCache();

		// layers.add(createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition));
		TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapView.getModel().mapViewPosition,
				mapFile);
		BoundingBox boundingBox = tileRendererLayer.getMapDatabase().getMapFileInfo().boundingBox;
		layers.add(tileRendererLayer);
		if (SHOW_DEBUG_LAYERS) {
			layers.add(new TileGridLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
			layers.add(new TileCoordinatesLayer(GRAPHIC_FACTORY, mapView.getModel().displayModel));
		}
		return boundingBox;
	}
	
	private static MapView createMapView() {
		MapView mapView = new MapView();
		mapView.getMapScaleBar().setVisible(true);
		mapView.getFpsCounter().setVisible(true);
		mapView.addComponentListener(new MapViewComponentListener(mapView, mapView.getModel().mapViewDimension));

		MouseEventListener mouseEventListener = new MouseEventListener(mapView.getModel());
		mapView.addMouseListener(mouseEventListener);
		mapView.addMouseMotionListener(mouseEventListener);
		mapView.addMouseWheelListener(mouseEventListener);

		return mapView;
	}

	private static TileCache createTileCache() {
		TileCache firstLevelTileCache = new InMemoryTileCache(64);
		File cacheDirectory = new File(System.getProperty("java.io.tmpdir"), "mapsforge");
		TileCache secondLevelTileCache = new FileSystemTileCache(1024, cacheDirectory, GRAPHIC_FACTORY);
		return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
	}

	@SuppressWarnings("unused")
	private static Layer createTileDownloadLayer(TileCache tileCache, MapViewPosition mapViewPosition) {
		TileSource tileSource = OpenStreetMapMapnik.INSTANCE;
		TileDownloadLayer tileDownloadLayer = new TileDownloadLayer(tileCache, mapViewPosition, tileSource,
				GRAPHIC_FACTORY);
		tileDownloadLayer.start();
		return tileDownloadLayer;
	}

	@SuppressWarnings("unused")
	private static TileRendererLayer createTileRendererLayer(TileCache tileCache, MapViewPosition mapViewPosition,
			File mapFile) {
		boolean isTransparent = false;
		TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapViewPosition, isTransparent,
				GRAPHIC_FACTORY);
		tileRendererLayer.setMapFile(mapFile);
		tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
		return tileRendererLayer;
	}

	private static File getMapFile(String args) {
		File mapFile = new File(args);
		if (!mapFile.exists()) {
			throw new IllegalArgumentException("file does not exist: " + mapFile);
		} else if (!mapFile.isFile()) {
			throw new IllegalArgumentException("not a file: " + mapFile);
		} else if (!mapFile.canRead()) {
			throw new IllegalArgumentException("cannot read file: " + mapFile);
		}

		return mapFile;
	}

	
}
