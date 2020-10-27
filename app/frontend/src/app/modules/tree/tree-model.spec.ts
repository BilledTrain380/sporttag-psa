import { combineLatest, Observable } from "rxjs";

import { TreeCheckNodeModel } from "./tree-model";

// tslint:disable: no-magic-numbers
describe("TreeModel", () => {
  const VEGETABLES = "Vegetables";
  const GREEN = "Green";
  const BROCCOLI = "Broccoli";
  const BRUSSELS = "Brussels sprouts";
  const ORANGE = "Orange";
  const PUMPKINS = "Pumpkins";
  const CARROTS = "Carrots";

  describe("TreeBuilder", () => {
    it("should build a tree check node model", () => {
      const treeModel = TreeCheckNodeModel.newBuilder()
        .setLabel(VEGETABLES)
        .addNode(builder =>
                   builder
                     .setLabel(GREEN)
                     .addLeafNode(BROCCOLI)
                     .addLeafNode(BRUSSELS))
        .addNode(builder =>
                   builder
                     .setLabel(ORANGE)
                     .addLeafNode(PUMPKINS)
                     .addLeafNode(CARROTS))
        .build();

      expect(treeModel.label)
        .toBe(VEGETABLES);
      expect(treeModel.nodes.length)
        .toBe(2);

      const green = treeModel.nodes[0];
      expect(green.label)
        .toBe(GREEN);
      expect(green.nodes.length)
        .toBe(2);

      const broccoli = green.nodes[0];
      expect(broccoli.label)
        .toBe(BROCCOLI);
      expect(broccoli.nodes.length)
        .toBe(0);

      const brussels = green.nodes[1];
      expect(brussels.label)
        .toBe(BRUSSELS);
      expect(brussels.nodes.length)
        .toBe(0);

      const orange = treeModel.nodes[1];
      expect(orange.label)
        .toBe(ORANGE);
      expect(orange.nodes.length)
        .toBe(2);

      const pumpkins = orange.nodes[0];
      expect(pumpkins.label)
        .toBe(PUMPKINS);
      expect(pumpkins.nodes.length)
        .toBe(0);

      const carrots = orange.nodes[1];
      expect(carrots.label)
        .toBe(CARROTS);
      expect(carrots.nodes.length)
        .toBe(0);

      treeModel.close();
    });
  });

  describe("TreeCheckNodeModel", () => {
    let treeNode: TreeCheckNodeModel;

    function allCheckedStatesOf(treeNodeModel: TreeCheckNodeModel): Array<Observable<boolean | undefined>> {
      const allCheckedStates = [treeNodeModel.isChecked$];

      treeNodeModel.nodes
        .forEach(node => {
          allCheckedStates.push(node.isChecked$);
          node.nodes.forEach(leafNode => allCheckedStates.push(leafNode.isChecked$));
        });

      return allCheckedStates;
    }

    beforeEach(() => {
      treeNode = TreeCheckNodeModel.newBuilder()
        .setLabel(VEGETABLES)
        .addNode(builder =>
                   builder
                     .setLabel(GREEN)
                     .addLeafNode(BROCCOLI)
                     .addLeafNode(BRUSSELS))
        .addNode(builder =>
                   builder
                     .setLabel(ORANGE)
                     .addLeafNode(PUMPKINS)
                     .addLeafNode(CARROTS))
        .build();
    });

    afterEach(() => {
      treeNode.close();
    });

    it("should check all models when root model is checked", done => {
      treeNode.isChecked = true;

      treeNode.isChecked$
        .subscribe(value => {
          expect(value)
            .withContext("IsChecked state")
            .toBeTrue();

          done();
        });
    });

    it("should check all sub models and intermediate all parent models when a model is checked", done => {
      treeNode.nodes[0].isChecked = true;

      combineLatest(allCheckedStatesOf(treeNode))
        .subscribe(values => {
          expect(values)
            .toEqual([undefined, true, true, true, false, false, false]);

          done();
        });
    });
  });
});
