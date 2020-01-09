export interface IPayment {
  id?: number;
  userActive?: string;
}

export class Payment implements IPayment {
  constructor(public id?: number, public userActive?: string) {}
}
