import { combineLatest, Observable } from "rxjs";
import { switchMap, take } from "rxjs/operators";

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
        .addNode(TreeCheckNodeModel.newBuilder()
                   .setLabel(GREEN)
                   .addLeafNode(BROCCOLI)
                   .addLeafNode(BRUSSELS))
        .addNode(TreeCheckNodeModel.newBuilder()
                   .setLabel(ORANGE)
                   .addLeafNode(PUMPKINS)
                   .addLeafNode(CARROTS))
        .build();

      expect(treeModel.label)
        .toBe(VEGETABLES);
      expect(treeModel.nodes.length)
        .toBe(1);
      expect(treeModel.nodes[0].length)
        .toBe(2);

      const green = treeModel.nodes[0][0];
      expect(green.label)
        .toBe(GREEN);
      expect(green.nodes.length)
        .toBe(1);
      expect(green.nodes[0].length)
        .toBe(2);

      const broccoli = green.nodes[0][0];
      expect(broccoli.label)
        .toBe(BROCCOLI);
      expect(broccoli.nodes.length)
        .toBe(0);

      const brussels = green.nodes[0][1];
      expect(brussels.label)
        .toBe(BRUSSELS);
      expect(brussels.nodes.length)
        .toBe(0);

      const orange = treeModel.nodes[0][1];
      expect(orange.label)
        .toBe(ORANGE);
      expect(orange.nodes.length)
        .toBe(1);
      expect(orange.nodes[0].length)
        .toBe(2);

      const pumpkins = orange.nodes[0][0];
      expect(pumpkins.label)
        .toBe(PUMPKINS);
      expect(pumpkins.nodes.length)
        .toBe(0);

      const carrots = orange.nodes[0][1];
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

      treeNodeModel.flatNodes
        .forEach(node => {
          allCheckedStates.push(node.isChecked$);
          node.flatNodes.forEach(leafNode => allCheckedStates.push(leafNode.isChecked$));
        });

      return allCheckedStates;
    }

    beforeEach(() => {
      treeNode = TreeCheckNodeModel.newBuilder()
        .setLabel(VEGETABLES)
        .addNode(TreeCheckNodeModel.newBuilder()
                   .setLabel(GREEN)
                   .addLeafNode(BROCCOLI)
                   .addLeafNode(BRUSSELS))
        .addNode(TreeCheckNodeModel.newBuilder()
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

    it("should  indeterminate all parent models when a sub model is checked", done => {
      treeNode.flatNodes[0].flatNodes[0].isChecked = true;

      combineLatest(allCheckedStatesOf(treeNode))
        .subscribe(values => {
          expect(values)
            .toEqual([undefined, undefined, true, false, false, false, false]);

          done();
        });
    });

    it("should check all sub models when a parent model is checked", done => {
      treeNode.flatNodes[0].isChecked = true;

      combineLatest(allCheckedStatesOf(treeNode))
        .subscribe(values => {
          expect(values)
            .toEqual([undefined, true, true, true, false, false, false]);

          done();
        });
    });

    it("should consider changes in parent as well in sub models", done => {
      treeNode.isChecked = true;

      combineLatest(allCheckedStatesOf(treeNode))
        .pipe(take(1))
        .pipe(switchMap(values => {
          expect(values)
            .toEqual([true, true, true, true, true, true, true]);

          // Change child and read states again
          treeNode.flatNodes[0].isChecked = false;

          return combineLatest(allCheckedStatesOf(treeNode));
        }))
        .subscribe(values => {
          expect(values)
            .toEqual([undefined, false, false, false, true, true, true]);

          done();
        });
    });
  });
});
