
package com.LibreriaMV0802.servicios;

import com.LibreriaMV0802.entidades.Autor;
import com.LibreriaMV0802.entidades.Foto;
import com.LibreriaMV0802.errores.ErrorServicio;
import com.LibreriaMV0802.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AutorService {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private FotoService fotoService;

    @Transactional
    public Autor cargarAutorS(String nombreForm, String bioForm, MultipartFile archivo) throws Exception {
        try {
            if (nombreForm == null || nombreForm.trim().isEmpty()) {
                throw new ErrorServicio("El nombre debe tener al menos una letra.");
            }
            if (!autorRepositorio.buscarAutorXNombreCompletoR(nombreForm).isEmpty()) {
                throw new ErrorServicio("Ya existe un autor con ese nombre.");
            }
            Autor autor = new Autor();
            autor.setNombre(nombreForm);
            autor.setBiografia(bioForm);
            autor.setAlta(true);
            if (!archivo.isEmpty()) {
                Foto foto = fotoService.guardarFoto(archivo);
                autor.setFoto(foto);
            }
            return autorRepositorio.save(autor);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional
    public void editarAutorS(Long idForm, String nombreForm, String bioForm, MultipartFile archivo) throws Exception {
        try {
            if (nombreForm == null || nombreForm.trim().isEmpty()) {
                throw new ErrorServicio("El nombre debe tener al menos una letra.");
            }
            Autor autor = autorRepositorio.findById(idForm).get();
            autor.setNombre(nombreForm);
            autor.setBiografia(bioForm);
            if (!archivo.isEmpty()) {
                Foto foto = fotoService.guardarFoto(archivo);
                autor.setFoto(foto);
            }
            autorRepositorio.save(autor);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void eliminarAutorS(Long id) throws Exception {
        autorRepositorio.delete(buscarXId(id));
    }
  
    public Autor buscarXId(Long id) throws Exception {
        Optional<Autor> autorOptional = autorRepositorio.findById(id);
        if (!autorOptional.isPresent()) {
            throw new ErrorServicio("El número ingresado no corresponde a ningún autor.");
        }
        return autorOptional.get();
    }
    
    public List<Autor> listarAutoresAlfS() {
        return autorRepositorio.listarAutoresAlfR();
    }
    
}
