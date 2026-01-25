package nl.tno.oorti.impl;

/**
 * @author bergtwvd
 */
class ExecutionContext {

  private final ObjectClassManager ocm;
  private final InteractionClassManager icm;
  private final ObjectInstanceManager oim;

  public ExecutionContext(
      ObjectClassManager ocm, InteractionClassManager icm, ObjectInstanceManager oim) {
    this.ocm = ocm;
    this.icm = icm;
    this.oim = oim;
  }

  public ObjectClassManager getOcm() {
    return this.ocm;
  }

  public InteractionClassManager getIcm() {
    return this.icm;
  }

  public ObjectInstanceManager getOim() {
    return this.oim;
  }
}