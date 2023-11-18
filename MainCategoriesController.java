package com.atom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.atom.entity.ORSMainCategories;
import com.atom.service.MainCategoriesService;

@Controller
@RequestMapping("/admin/*")
public class MainCategoriesController {
	protected static Logger log = Logger.getLogger(MainCategoriesController.class);

	@Autowired
	private MainCategoriesService mainCatgService;
	private Path path;

	public MainCategoriesController() {
	//	System.out.println("MainCategories Controller");
	}

	@RequestMapping(value = "/homePage", method = RequestMethod.GET)
	public ModelAndView adminIndex(ModelAndView model) {
		model.setViewName("adminIndex");
		return model;
	}

	@RequestMapping(value = "/mainCategories", method = RequestMethod.GET)
	public ModelAndView listMainCatgs(ModelAndView model) {
		List<ORSMainCategories> listMainCatgs = mainCatgService.getAllMainCatgs();
		model.addObject("listMainCatgs", listMainCatgs);
		model.setViewName("displayMainCatgs");
		return model;
	}

	@RequestMapping(value = "/addMainCategories", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {

		ORSMainCategories catg = new ORSMainCategories();
		model.addObject("catg", catg);
		model.setViewName("addMainCatgs");
		return model;
	}

	@RequestMapping(value = "/saveMainCatergories", method = RequestMethod.POST)
	public ModelAndView saveMainCatg(@Valid @ModelAttribute("catg") ORSMainCategories mainCatg,
			BindingResult result, HttpServletRequest request,Model map) throws IllegalStateException, IOException {

		
		
		String replaceName = mainCatg.getOrs_mc_category_name().replace(" ", "_");
		if (result.hasErrors()) {
			return new ModelAndView("addMainCatgs");

		}
		boolean duplicateCatgName = mainCatgService.allowedDuplicateMain(mainCatg.getOrs_mc_category_name() , mainCatg.getOrs_mc_id());
		if (mainCatg.getOrs_mc_id() == null) {
			if (duplicateCatgName) {
				mainCatg.setOrs_mc_status("1");
				mainCatg.setOrs_mc_create_date(dateAndTime());
				MultipartFile catgImg = mainCatg.getCatgImage();
				String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				File folder = new File(rootDirectory + "/WEB-INF/resources/images/Categories");
				if (!folder.exists()) {
					if (folder.mkdirs()) {
						System.out.println("Directory is created!");

					} else {
						System.out.println("Failed to create directory!");

					}
				}
				System.out.println("folder   " + folder);
				path = Paths.get(folder + File.separator + replaceName + ".png");
				if (catgImg != null && !catgImg.isEmpty()) {

					log.warn("Insert ");
					catgImg.transferTo(new File(path.toString()));
					mainCatg.setOrs_mc_imageName(replaceName);
					// mainCatgService.addMainCatgs(mainCatg);
				} else {
					mainCatg.setOrs_mc_imageName(null);
					// mainCatgService.addMainCatgs(mainCatg);
				}

				mainCatgService.addMainCatgs(mainCatg);
			} else {
				map.addAttribute("errorMesg", "Main Category Name was already exist");
				return new ModelAndView("addMainCatgs");
			}

		} else {
			mainCatg.setOrs_mc_status("1");
			if (duplicateCatgName) {
				MultipartFile catgImg = mainCatg.getCatgImage();
				String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				File folder = new File(rootDirectory + "/WEB-INF/resources/images/Categories");
				if (!folder.exists()) {
					folder.mkdir();
				}
				System.out.println("folder   " + folder);
				path = Paths.get(folder + File.separator + replaceName + ".png");
				log.warn("mainCatg.getOrs_mc_imageName()   " + mainCatg.getOrs_mc_imageName());
				if (mainCatg.getOrs_mc_imageName() == null) {
					log.warn("Updating the Image");
					if (catgImg != null && !catgImg.isEmpty()) {
						log.warn("Insert ");
						if (Files.exists(path)) {
							try {
								log.warn("Image path" + path);
								Files.delete(path);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						catgImg.transferTo(new File(path.toString()));
						mainCatg.setOrs_mc_imageName(replaceName);
						mainCatgService.addMainCatgs(mainCatg);
					} else {
						mainCatg.setOrs_mc_imageName(null);
						mainCatgService.addMainCatgs(mainCatg);
					}
				} else {
					log.warn("Updating the Image In else block");
					if (catgImg != null && !catgImg.isEmpty()) {
						if (Files.exists(path)) {
							try {
								Files.delete(path);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						catgImg.transferTo(new File(path.toString()));

						log.warn("Updating the Image In else block  replaceName   " + replaceName);
						mainCatg.setOrs_mc_imageName(replaceName);
						mainCatgService.addMainCatgs(mainCatg);
					} else {

					}

				}
				//mainCatgService.addMainCatgs(mainCatg);
				mainCatgService.updateCatgs(mainCatg);
			}else {
			map.addAttribute("errorMesg", "Main Category Name was already exist");
			return new ModelAndView("addMainCatgs");
		}

		}
		

		return new ModelAndView("redirect:/admin/mainCategories");
	}

	@ModelAttribute("alphabetOrder")
	public Map<String, String> getAlphabetOrder() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 65; i <= 90; i++) {
			map.put(Character.toString((char) i), Character.toString((char) i));
		}
		return map;
	}
	
	@ModelAttribute("numeric")
	public Map<Integer, Integer> getnumericOrder() {
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		for (int i = 1; i <= 100; i++) {
			map.put(i,  i);
		}
		return map;
	}

	public String dateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}

	@RequestMapping(value = "/deleteMainCatgs", method = RequestMethod.GET)
	public ModelAndView deleteMainCatgs(@RequestParam int id, ModelAndView model) {
		mainCatgService.deleteMainCatg(id);
		return new ModelAndView("redirect:/admin/mainCategories");
	}

	@RequestMapping(value = "/editMainCatgs")
	public ModelAndView editMainCatgs(@RequestParam int id, ModelAndView model) {
		System.out.println("Edit Id" + id);
		ORSMainCategories main = mainCatgService.getMainCatg(id);
		model.addObject("catg", main);
		model.setViewName("addMainCatgs");

		return model;
	}
	
	
	@RequestMapping(value = "/deleteMainImage", method = RequestMethod.GET)
	public ModelAndView deleteMainImage(@RequestParam Integer id,@RequestParam String mainImg,HttpServletRequest request) {
		log.warn(" Prd Img id+ "+ id  +"   Image Name "+mainImg);
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		File partnerImg = new File(rootDirectory + "/WEB-INF/resources/images/Categories");
		log.warn("delete client image "+mainImg);
		
		System.out.println("partnerImg     "+ partnerImg );
		path = Paths.get(partnerImg + File.separator + mainImg+".png");
		log.warn("filePartners     "  +  path);
		if (Files.exists(path)) {
			try {
				log.warn("Image path" + path);
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		mainCatgService.deleteMainImg(id);
		return new ModelAndView("redirect:/admin/mainCategories");
	}
	
	/*@RequestMapping(value = "/mainCategories", method = RequestMethod.GET)
	public ModelAndView displayMainCatgs(ModelAndView model) {
		List<ORSMainCategories> listMainCatgs = mainCatgService.getAllMainCatgs().stream().limit(5).collect(Collectors.toList());
		log.warn("listMainCatgs  Size   "+listMainCatgs.size()) ;
		model.addObject("listMainCatgs", listMainCatgs);
		model.setViewName("completeProd");
		return model;
	}*/
}
