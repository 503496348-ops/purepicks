#!/usr/bin/env python3
"""PurePicks MCP Client entrypoint.

Delegates to server.py so both `python main.py` and `python server.py` work.
"""
from __future__ import annotations

import uvicorn


def main() -> None:
    uvicorn.run("server:app", host="0.0.0.0", port=8188, reload=False)


if __name__ == "__main__":
    main()
