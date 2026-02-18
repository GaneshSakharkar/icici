package com.imps.icici;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.imps.icici.dto.BankIFSC;
import com.imps.icici.dto.IFSCDetailsDTO;
import com.imps.icici.entity.IfscBranchCode;
import com.imps.icici.entity.RoleEntity;
import com.imps.icici.exception.ResourceAlreadyExistException;
import com.imps.icici.repository.IfscRepository;
import com.imps.icici.repository.RoleRepository;

@SpringBootApplication
public class IciciApplication {

	
	@Autowired
	private RoleRepository oRoleRepository;
	
	@Autowired
	private IfscRepository oIfscRepository;
	
	//@Value("${csv.filepath}")
	private String filepath = "D:\\Prevoyance-JavaTranning\\csv_file\\bank_details.xlsx";
	
	public static void main(String[] args) {
		SpringApplication.run(IciciApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner initialzeRoles() {
		return args ->{
			if(!oRoleRepository.existsByRoleName("USER")) {
				RoleEntity role = new RoleEntity();
				role.setRoleName("USER");
				oRoleRepository.save(role);
			}
			if(!oRoleRepository.existsByRoleName("ADMIN")) {

				RoleEntity role = new RoleEntity();
				role.setRoleName("ADMIN");
				oRoleRepository.save(role);
			}
			if(!oRoleRepository.existsByRoleName("MANAGER")) {
				RoleEntity role = new RoleEntity();
				role.setRoleName("MANAGER");
				oRoleRepository.save(role);
			}
		};
	}
	
	
	@Bean
	public CommandLineRunner initiateIfscExcel() {
		
		return args->{
			try(Workbook workbook = new XSSFWorkbook(new FileInputStream(filepath))) {
					
				List<IfscBranchCode> listIfsc = new ArrayList<>();
				
				Sheet sheet = workbook.getSheetAt(0);
				
				for(int i=1;i<sheet.getLastRowNum();i++) {
					
					IfscBranchCode ifsc = new IfscBranchCode();
					
					Row row = sheet.getRow(i);
					
					ifsc.setBranchName(row.getCell(0).getStringCellValue());
					ifsc.setIfsc(row.getCell(1).getStringCellValue());
					
					String pincode = row.getCell(2).getStringCellValue();
	
					Long pin= Long.parseLong(pincode);
					
					
					Optional<IfscBranchCode> validatePincode =  oIfscRepository.findByPincode(pin);
					if(validatePincode.isPresent()) {
						
						throw new ResourceAlreadyExistException("");
						
					}
					
					ifsc.setPincode(pin);
					listIfsc.add(ifsc);
					
				}
				
				oIfscRepository.saveAllAndFlush(listIfsc);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}; 
	
	}
}
