using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace Core.Db.Migrations
{
    public partial class Addfavorites : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Favorite-Place-Links",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    PlaceId = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Favorite-Place-Links", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Favorite-Place-Links_Places_PlaceId",
                        column: x => x.PlaceId,
                        principalSchema: "citylogia",
                        principalTable: "Places",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Favorite-Place-Links_Users_UserId",
                        column: x => x.UserId,
                        principalSchema: "citylogia",
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Favorite-Place-Links_PlaceId",
                schema: "citylogia",
                table: "Favorite-Place-Links",
                column: "PlaceId");

            migrationBuilder.CreateIndex(
                name: "IX_Favorite-Place-Links_UserId",
                schema: "citylogia",
                table: "Favorite-Place-Links",
                column: "UserId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Favorite-Place-Links",
                schema: "citylogia");
        }
    }
}
