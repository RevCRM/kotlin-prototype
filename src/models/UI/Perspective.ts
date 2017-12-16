
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';
import { RevCRMModel } from '../RevCRMModel';

@ApiOperations(['read'])
export class Perspective extends RevCRMModel<Perspective> {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        model: string;

}
