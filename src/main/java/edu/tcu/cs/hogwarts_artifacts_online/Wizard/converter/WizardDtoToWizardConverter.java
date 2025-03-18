package edu.tcu.cs.hogwarts_artifacts_online.Wizard.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.tcu.cs.hogwarts_artifacts_online.Wizard.Wizard;
import edu.tcu.cs.hogwarts_artifacts_online.Wizard.dto.WizardDto;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto,Wizard> {

	@Override
	public Wizard convert(WizardDto source) {
		Wizard newWizard=new Wizard();
		 //newWizard.setId(source.id());
		newWizard.setName(source.name());
		return newWizard;
	}
	

}
