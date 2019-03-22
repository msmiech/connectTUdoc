package at.connectTUdoc.backend.dto;

import org.modelmapper.ModelMapper;

/**
 * This class contains common used methods and objects for the DTO Layer
 */
abstract class AbstractDTO {

    final ModelMapper modelMapper = new ModelMapper();


}
