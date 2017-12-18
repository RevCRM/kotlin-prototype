
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';

@ApiOperations(['read'])
export class Menu {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        icon: string;

}
