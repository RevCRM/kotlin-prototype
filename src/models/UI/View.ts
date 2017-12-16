
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';
import { RevCRMModel } from '../RevCRMModel';

@ApiOperations(['read'])
export class View extends RevCRMModel<View> {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        handler: string;

}
