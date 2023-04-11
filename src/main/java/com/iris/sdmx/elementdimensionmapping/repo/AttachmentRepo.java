/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iris.sdmx.elementdimensionmapping.controller.Attachment;

/**
 * @author sajadhav
 *
 */
public interface AttachmentRepo extends JpaRepository<Attachment, Long> {

}
