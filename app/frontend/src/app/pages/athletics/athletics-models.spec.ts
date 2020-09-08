import { BALLWURF, ResultDto, SCHNELLLAUF } from "../../dto/athletics";

import { ResultModel } from "./athletics-models";

describe("AthleticsModels", () => {
  describe("ResultDto", () => {
    describe("on form control validation", () => {
      it("should add int number validation when a unit with factor 1 is given", () => {
        const resultDto: ResultDto = {
          id: 1,
          value: 25,
          relativeValue: "25",
          points: 100,
          discipline: {
            name: BALLWURF,
            hasDistance: false,
            hasTrials: false,
            unit: {
              name: "Meter",
              factor: 1,
            },
          },
        };

        const model = ResultModel.fromDto(resultDto);

        model.displayValueControl.setValue("11");
        expect(model.displayValueControl.valid)
          .withContext("Display value control valid state")
          .toBeTrue();

        model.displayValueControl.setValue("11.25");
        expect(model.displayValueControl.valid)
          .withContext("Display value control valid state")
          .toBeFalse();
      });

      it("should add float number validation when a unit with factor greater than 1 is given", () => {
        const resultDto: ResultDto = {
          id: 1,
          value: 1125,
          relativeValue: "11.25",
          points: 213,
          distance: "2m",
          discipline: {
            name: SCHNELLLAUF,
            hasDistance: true,
            hasTrials: false,
            unit: {
              name: "Seconds",
              factor: 100,
            },
          },
        };

        const model = ResultModel.fromDto(resultDto);
        model.displayValueControl.setValue("11.25");

        expect(model.displayValueControl.valid)
          .withContext("Display value control valid state")
          .toBeTrue();

        model.displayValueControl.setValue("11");

        expect(model.displayValueControl.valid)
          .withContext("Display value control valid state")
          .toBeTrue();
      });
    });

    describe("on mapping the dto", () => {
      it("should add a prepned text when a distance is given", () => {
        const resultDto: ResultDto = {
          id: 1,
          value: 1012,
          relativeValue: "10.12",
          points: 187,
          distance: "2m",
          discipline: {
            name: SCHNELLLAUF,
            hasDistance: true,
            hasTrials: false,
            unit: {
              name: "Seconds",
              factor: 100,
            },
          },
        };

        const model = ResultModel.fromDto(resultDto);

        expect(model.id)
          .toBe(resultDto.id);
        expect(model.displayValueControl.value)
          .toBe("10.12");
        expect(model.points)
          .toBe(resultDto.points);
        expect(model.prependText)
          .toBe("Distance: 2m");
        expect(model.appendText)
          .toBe("Seconds");
      });

      it("should not add a prepend text when no distance is given", () => {
        const resultDto: ResultDto = {
          id: 1,
          value: 25,
          relativeValue: "25",
          points: 100,
          discipline: {
            name: BALLWURF,
            hasDistance: false,
            hasTrials: false,
            unit: {
              name: "Meter",
              factor: 1,
            },
          },
        };

        const model = ResultModel.fromDto(resultDto);

        expect(model.id)
          .toBe(resultDto.id);
        expect(model.displayValueControl.value)
          .toBe("25");
        expect(model.points)
          .toBe(resultDto.points);
        expect(model.prependText)
          .toBe("");
        expect(model.appendText)
          .toBe("Meter");
      });
    });
  });
});
