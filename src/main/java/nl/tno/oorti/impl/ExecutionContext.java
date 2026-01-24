package nl.tno.oorti.impl;

/**
 * @author bergtwvd
 */
class ExecutionContext {

  public ExecutionContext(
      ObjectClassManager ocm, InteractionClassManager icm, ObjectInstanceManager oim) {
    this.ocm = ocm;
    this.icm = icm;
    this.oim = oim;
  }

  private final ObjectClassManager ocm;
  private final InteractionClassManager icm;
  private final ObjectInstanceManager oim;

  public ObjectClassManager getOcm() {
    return ocm;
  }

  public InteractionClassManager getIcm() {
    return icm;
  }

  public ObjectInstanceManager getOim() {
    return oim;
  }
}