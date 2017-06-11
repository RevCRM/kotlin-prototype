
import * as rev from 'rev-models';

export class Company extends rev.Model {
    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.TextField()
        name: string;
    @rev.URLField()
        url: string;

    validate(vc: rev.IValidationContext) {
        if (this.name.toLowerCase().includes('test')) {
            vc.result.addFieldError('name', 'Name cannot include the word "test"');
        }
    }

}
