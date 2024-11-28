package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.controllers;

import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities.Province;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.entities.Region;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProvinceControllerTest {

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private ProvinceController provinceController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListProvinces(){
        //Crear una listas de provincias simuladas
        List<Province> provinces=new ArrayList<>();
        provinces.add(new Province());
        provinces.add(new Province());

        //Configurar el comportamiento del Mock
        when(provinceRepository.findAll()).thenReturn(provinces);

        //Llamar al metodo bajo prueba
        String viewName=provinceController.listProvinces(model);

        //verificar que se llamo al metodo correcto
        verify(model).addAttribute("province",provinces);
        assertEquals("province",viewName);
    }

    @Test
    public void testShowNewForm(){
        //Crear una listas de provincias simuladas
        List<Region> regions=new ArrayList<>();
        regions.add(new Region());
        regions.add(new Region());

        //Configurar el comportamiento del Mock
        when(regionRepository.findAll()).thenReturn(regions);

        //Llamar al metodo bajo prueba
        String viewName=provinceController.showNewForm(model);

        //verificar que se llamo al metodo correcto
        verify(model).addAttribute(eq("province"),any(Province.class));
        verify(model).addAttribute("regions",regions);
        assertEquals("province-form",viewName);
    }

    @Test
    public void testInsertProvince(){
        //Crear una provincia valida
        Province province=new Province();
        province.setCode("TEST");

        //No hay errores de validacion
        BindingResult result=mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        //configurar el comportamiento del mock
        when(provinceRepository.existsProvinceByCode(province.getCode())).thenReturn(false);

        //Llamar al metodo bajo prueba
        String redirectView=provinceController.insertProvince(province,result,mock(RedirectAttributes.class),
                model,Locale.getDefault());

        //verificar que se guarda la provincia y la vista es la correcta
        verify(provinceRepository).save(province);
        assertEquals("redirect:/provinces",redirectView);
    }

    @Test
    public void testInsertProvinceWithValidationsErrors(){
        Province province=new Province();
        BindingResult result=mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        String viewName=provinceController.insertProvince(province,result,
                mock(RedirectAttributes.class),model,Locale.getDefault());

        verify(model).addAttribute("regions",regionRepository.findAll());
        assertEquals("province-form",viewName);
    }

    @Test
    public void testDeleteProvince(){
        //ID de la provincia a eliminar
        Long provinceId=1L;

        //Configuracion el comportamniento del mock
        doNothing().when(provinceRepository).deleteById(provinceId);

        //Llamar al metodo bajo prueba
        String redirectView=provinceController.deleteProvince(provinceId,redirectAttributes);

        //verificar que se haya llamado al metodo deleteById() del repositorio
        verify(provinceRepository).deleteById(provinceId);

        //verificar que la vista devuelta sea la correcta
        assertEquals("redirect:/provinces",redirectView);
    }

    @Test
    public void testDeleteProvinceWithException(){
        //ID de la provincia a eliminar
        Long provinceId=1L;

        //Configuracion el comportamniento del mock para lanzar una excepcion
        doThrow(new RuntimeException("Error al eliminar la provincia.")).
                when(provinceRepository).deleteById(provinceId);

        //Llamar al metodo bajo prueba
        String redirectView=provinceController.deleteProvince(provinceId,redirectAttributes);

        //verificar que se haya llamado al metodo deleteById() del repositorio
        verify(provinceRepository).deleteById(provinceId);

        //verificar que se haya agregado un mensaje de error a los atributos de redireccion
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"),anyString());

        //verificar que la vista devuelta sea la correcta
        assertEquals("redirect:/provinces",redirectView);
    }

    @Test
    public void testShowEditForm(){
        //ID de la provincia a editar
        Long provinceId=1L;

        //Crear una provincia simulada
        Province provinces=new Province();
        provinces.setId(provinceId);
        provinces.setCode("TEST");

        //Crear una listas de provincias simuladas
        List<Region> regions=new ArrayList<>();
        regions.add(new Region());

        //Configurar el comportamiento del mock
        when(provinceRepository.findById(provinceId)).thenReturn(Optional.of(provinces));
        when(regionRepository.findAll()).thenReturn(regions);

        //Llamar al metodo bajo prueba
        String viewName=provinceController.showEditForm(provinceId,model);

        //verificar que se añaden los atributos correctos al modelo
        verify(model).addAttribute("province",provinces);
        verify(model).addAttribute("regions",regions);

        //verificar que la vista devuelta sea la correcta
        assertEquals("province-form",viewName);
    }

    @Test
    public void testUpdateProvince(){
        //Crear una provincia valida
        Province province=new Province();
        province.setId(1L);
        province.setCode("TEST");

        //No hay errores de validacion
        BindingResult result=mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        //configurar el comportamiento del mock
        when(provinceRepository.existsProvinceByCodeAndNotId(province.getCode(),province.getId())).thenReturn(false);

        //Llamar al metodo bajo prueba
        String redirectView=provinceController.updateProvince(province,result,
                mock(RedirectAttributes.class),model,Locale.getDefault());

        //verificar que se guarda la provincia y la vista es la correcta
        verify(provinceRepository).save(province);
        assertEquals("redirect:/provinces",redirectView);
    }

    @Test
    public void testUpdateProvinceWithValidationErrors(){
        //Crear una provincia con errores de validacion
        Province province=new Province();
        province.setId(1L);

        //
        //Simular errores de validacion
        BindingResult result=mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        //Crear una listas de regiones simuladas
        List<Region> regions=new ArrayList<>();
        regions.add(new Region());

        //configurar el comportamiento del mock para devolver la lista de regiones
        when(regionRepository.findAll()).thenReturn(regions);

        //Llamar al metodo bajo prueba
        String viewName=provinceController.updateProvince(province,result,
                mock(RedirectAttributes.class),model,Locale.getDefault());

        //verificar que la lista de regiones se ha agregado al modelo
        verify(model).addAttribute("regions",regions);
        assertEquals("province-form",viewName);
    }

    @Test
    public void testUpdateProvinceWithExistingCode(){
        //Crear una provincia con un codigo que ya existe
        Province province=new Province();
        province.setId(1L);
        province.setCode("DUPLICATE");

        //No hay errores de validacion
        BindingResult result=mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        //simular que ya existe una provincia con el mismo codigo
        when(provinceRepository.existsProvinceByCodeAndNotId(province.getCode(),province.getId())).thenReturn(true);

        //Crear un RedirectAttributes simulado
        RedirectAttributes redirectAttributes=mock(RedirectAttributes.class);

        //Configurar el comportamiento del messageSource
        when(messageSource.getMessage("msg.province-controller.update.codeExist",null,
                Locale.getDefault())).thenReturn("Codigo ya existente.");

        //Llamar al metodo bajo prueba
        String redirectView=provinceController.updateProvince(province,result,redirectAttributes,model,Locale.getDefault());

        //Verificar que se haya agregado un mensaje de error a los atributos de redireccion
        verify(redirectAttributes).addFlashAttribute("errorMessage","Codigo ya existente.");

        //Verificar que la vista devuelta sea la correcta(redireccion al formulario de ediccion)
        assertEquals("redirect:/provinces/edit?id="+province.getId(),redirectView);
    }

}