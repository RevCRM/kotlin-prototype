
import { ModelManager, InMemoryBackend } from 'rev-models';
import { UserLoginFormModel } from './User/forms/UserLoginForm';
import { Company } from './Company/Company';
import { SelectionList } from './SelectionList/SelectionList';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new InMemoryBackend());

clientModels.register(UserLoginFormModel);
clientModels.register(Company);
clientModels.register(SelectionList);
