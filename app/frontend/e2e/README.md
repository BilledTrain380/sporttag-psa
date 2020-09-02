# PSA e2e testing

Since PSA has an application state whenever the participation is open or
closed, some test must run while the participation is open, others must run
while the participation is closed.

To handle this behavior, each test which depends on this state must include
a special prefix in the test file name.

| Type   | Filename                      | Explanation                                   |
|:------ |:----------------------------: |:----------------------------------------------|
| Open   | `<test-name>.bpc.e2e-spec.ts` | `bpc` stands for `before participation closed`|
| Closed | `<test-name>.apc.e2e-spec.ts` | `apc` stands for `after participation closed` |
